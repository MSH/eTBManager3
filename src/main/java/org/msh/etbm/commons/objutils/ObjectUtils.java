package org.msh.etbm.commons.objutils;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * General utility functions involving an object
 * <p>
 * Created by rmemoria on 28/1/16.
 */
public class ObjectUtils {

    /**
     * Since there are just static methods, avoid others of creating this class
     */
    private ObjectUtils() {
        super();
    }

    /**
     * Read the property value of an object
     *
     * @param obj      the object to have its property read
     * @param property the property name to read
     * @return the property value, or {@link ObjectAccessException} if any error occurs
     */
    public static Object getProperty(Object obj, String property) {
        try {
            return PropertyUtils.getProperty(obj, property);
        } catch (Exception e) {
            throw new ObjectAccessException(obj, property, "Error reading property " + property, e);
        }
    }

    /**
     * Set a property value of an object
     *
     * @param obj      the object to have its property value set
     * @param property the property name to set its value
     * @param value    the value to set
     */
    public static void setProperty(Object obj, String property, Object value) {
        try {
            PropertyUtils.setProperty(obj, property, value);
        } catch (Exception e) {
            throw new ObjectAccessException(obj, property,
                    "Error writing property " + obj + '.' + property + " with value " + value, e);
        }
    }

    /**
     * Return property type of the given property in the given object
     *
     * @param obj      the object to have its property type returned
     * @param property the name of the property in the object
     * @return the property type
     */
    public static Class getPropertyType(Object obj, String property) {
        try {
            return PropertyUtils.getPropertyType(obj, property);
        } catch (Exception e) {
            throw new ObjectAccessException(obj, property,
                    "Error getting type of property " + property, e);
        }
    }

    /**
     * Create a new instance of a given class. The class must implement a constructor with
     * no arguments, otherwise a {@link ObjectAccessException} will be thrown
     *
     * @param clazz The class to create an instance from
     * @return the object instance of the given class
     */
    public static <E> E newInstance(Class<E> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new ObjectAccessException("Error when trying to create an instance of " + clazz, e);
        }
    }


    /**
     * Return the generic type assigned to the given class
     *
     * @param clazz     the class to get the generic type assigned to
     * @param typeindex the index of the generic type, when there are more than one, but must be 0
     *                  if there is just one single generic type or you want the first generic type
     * @return the generic class type assigned to the class
     */
    public static Class getGenericType(Class clazz, int typeindex) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            if (paramType.getActualTypeArguments().length > 0) {
                return (Class) paramType.getActualTypeArguments()[typeindex];
            }
        }
        return null;
    }


    /**
     * Get a class by its name
     *
     * @param className the full qualified class name
     * @return The class
     */
    public static Class forClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ObjectAccessException("Class not found: " + className, e);
        }
    }


    /**
     * Generate a hash of the object, hashing every property value using the JDK Objects.hash function
     *
     * @param obj
     * @return
     */
    public static int hash(Object obj) {
        if (obj == null || obj instanceof String || obj instanceof Number || obj instanceof Date ||
                obj instanceof Collection || obj.getClass().isArray()) {
            return Objects.hash(obj);
        }

        Map<String, Object> values = describeProperties(obj);
        Object[] vals = new Object[values.size()];
        int index = 0;
        for (Map.Entry<String, Object> prop : values.entrySet()) {
            vals[index] = prop.getValue();
            index++;
        }
        return Objects.hash(vals);
    }

    /**
     * Return a map containing the object property name and its values
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> describeProperties(Object obj) {
        Map<String, Object> values;
        try {
            values = PropertyUtils.describe(obj);

            PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(obj);

            // remove properties that don't have a get or a set
            for (PropertyDescriptor prop : props) {
                if (prop.getReadMethod() == null || prop.getWriteMethod() == null) {
                    values.remove(prop.getName());
                }
            }

        } catch (Exception e) {
            throw new ObjectAccessException("Error getting object properties", e);
        }

        return values;
    }

    /**
     * Convert an array of bytes to an UUID object
     *
     * @param val an array of bytes
     * @return instance of UUID
     */
    public static UUID bytesToUUID(byte[] val) {
        ByteBuffer bb = ByteBuffer.wrap(val);
        long high = bb.getLong();
        long low = bb.getLong();
        return new UUID(high, low);
    }

    /**
     * Convert an UUID instance to an array of bytes
     * @param uuid
     * @return
     */
    public static byte[] uuidAsBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }


    /**
     * Return the generic type declared in the field property of the class
     * @param beanClass
     * @param fieldName
     * @param typeIndex
     * @return
     */
    public static Class getPropertyGenericType(Class beanClass, String fieldName, int typeIndex) {
        Field field = findField(beanClass, fieldName);

        if (field == null) {
            throw new ObjectAccessException("Class field not found: [" + fieldName + "] in class " + beanClass);
        }

        return getPropertyGenericType(field, typeIndex);
    }

    /**
     * Return the generic type declared in the field instance
     * @param field the field containing the generic type declaration
     * @param typeIndex the declaration order of the generic type (zero is the first)
     * @return the generic type declaration, or null if no generic type is declared
     */
    public static Class getPropertyGenericType(Field field, int typeIndex) {
        Type type = field.getGenericType();

        if (!(type instanceof ParameterizedType)) {
            return null;
        }

        ParameterizedType ptype = (ParameterizedType)type;
        Type[] typeArgs = ptype.getActualTypeArguments();

        return (Class)typeArgs[typeIndex];
    }

    /**
     * Search for the instance of the field that declared the property in the given
     * class or its super classes
     * @param beanClass the bean class name
     * @param fieldName the field name to search for
     * @return the instance of Field or null if field is not found
     */
    public static Field findField(Class beanClass, String fieldName) {
        Class clazz = beanClass;

        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field: fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * Search for a method by its name in the bean class or, if not found, in the super classes
     * @param beanClass the class to search the method
     * @param methodName the method name
     * @return instance of the method from java reflection, or null if not found
     */
    public static Method findMethod(Class beanClass, String methodName) {
        Class clazz = beanClass;

        while (clazz != null && clazz != Object.class) {
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method: methods) {
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * Convert a string to an enum value of the given enum class
     * @param value the string representation of the enum value
     * @param enumClass the enum class
     * @param <E>
     * @return the enum value
     */
    public static <E extends Enum> E stringToEnum(String value, Class<E> enumClass) {
        Object[] vals = enumClass.getEnumConstants();

        for (Object val: vals) {
            if (val.toString().equals(value)) {
                return (E)val;
            }
        }
        return null;
    }


    /**
     * Copy the properties of the source object to the target object.
     * Source and target objects doesn't have to be of same type, and just shared
     * properties available in target are copied. Properties with the same name must
     * have the same type, otherwise an exception will be thrown
     *
     * @param source the source object
     * @param target the target object that will receive the property values from source
     */
    public static void copyObject(Object source, Object target) {
        Map<String, Object> props = describeProperties(source);
        Map<String, Object> targProps = describeProperties(target);

        for (Map.Entry<String, Object> entry: props.entrySet()) {
            String prop = entry.getKey();
            if (targProps.containsKey(prop)) {
                setProperty(target, prop, entry.getValue());
            }
        }
    }
}
