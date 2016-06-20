package org.msh.etbm.commons.objutils;

import org.apache.commons.beanutils.PropertyUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * General utility functions involving an object
 *
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
     * @param obj the object to have its property read
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
     * @param obj the object to have its property value set
     * @param property the property name to set its value
     * @param value the value to set
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
     * Create a new instance of a given class. The class must implement a constructor with
     * no arguments, otherwise a {@link ObjectAccessException} will be thrown
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
     * @param clazz the class to get the generic type assigned to
     * @param typeindex the index of the generic type, when there are more than one, but must be 0
     *                  if there is just one single generic type or you want the first generic type
     * @return the generic class type assigned to the class
     */
    public static Class getGenericType(Class clazz, int typeindex) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            if (paramType.getActualTypeArguments().length > 0) {
                return (Class)paramType.getActualTypeArguments()[typeindex];
            }
        }
        return null;
    }


    /**
     * Get a class by its name
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
        for (Map.Entry<String, Object> prop: values.entrySet()) {
            vals[index] = prop.getValue();
            index++;
        }
        return Objects.hash(vals);
    }

    /**
     * Return a map containing the object property name and its values
     * @param obj
     * @return
     */
    public static Map<String, Object> describeProperties(Object obj) {
        Map<String, Object> values;
        try {
            values = PropertyUtils.describe(obj);

            PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(obj);

            // remove properties that don't have a get or a set
            for (PropertyDescriptor prop: props) {
                if (prop.getReadMethod() == null || prop.getWriteMethod() == null) {
                    values.remove(prop.getName());
                }
            }

        } catch (Exception e) {
            throw new ObjectAccessException("Error getting object properties", e);
        }

        return values;
    }
}
