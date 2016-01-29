package org.msh.etbm.commons.objutils;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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


}
