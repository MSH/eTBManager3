package org.msh.etbm.commons.models.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.JSFunction;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rmemoria on 6/8/16.
 */
public class StandardJSONParser<E> {

    protected E parseInputStream(InputStream in) {
        Map<String, Object> props = parseJsonStream(in);

        Class beanClass = ObjectUtils.getGenericType(getClass(), 0);
        return (E)convertObject(props, beanClass);
    }

    protected Map<String, Object> parseJsonStream(InputStream in) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(in, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new ModelException(e);
        }
    }


    /**
     * Convert a Map with property and values to an object of the given bean class
     * @param props the list of properties and values
     * @param beanClass the bean class
     * @param <E>
     * @return the instance of the bean class
     */
    protected <E> E convertObject(Map<String, Object> props, Class<E> beanClass) {
        E bean = ObjectUtils.newInstance(beanClass);

        // set the values of the bean
        for (Map.Entry<String, Object> entry: props.entrySet()) {
            String propName = entry.getKey();
            Object value = entry.getValue();

            Object bean2 = getBean(bean, propName);

            if (bean2 == null) {
                throw new ModelException("Invalid property: " + propName);
            }

            Class propClass = ObjectUtils.getPropertyType(bean2, propName);
            if (propClass == null) {
                throw new ModelException("Invalid property: " + propName);
            }

            Field field = ObjectUtils.findField(bean2.getClass(), propName);
            value = convertValue(value, propClass, field);

            ObjectUtils.setProperty(bean2, propName, value);
        }

        return bean;
    }

    /**
     * Return the bean to use. Usually it is the own bean, but it allows to create a single list of
     * properties in JSON file to write to different bean objects
     * @param bean The original bean object
     * @param propName the property name
     * @return the bean to be used instead of the original
     */
    protected Object getBean(Object bean, String propName) {
        return bean;
    }

    protected <E> E convertValue(Object value, Class<E> targetClass, Field field) {
        // convert date type
        if (targetClass.isAssignableFrom(Date.class)) {
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            try {
                return (E)fmt.parse(value.toString());
            } catch (ParseException e) {
                throw new ModelException(e);
            }
        }

        if (targetClass == JSFuncValue.class) {
            return (E)parseJSFuncValue(value);
        }

        if (targetClass == JSFunction.class) {
            return (E)parseJSFunction(value);
        }

        if (Collection.class.isAssignableFrom(targetClass)) {
            return (E)parseCollection((Collection) value, targetClass, field);
        }

        if (value instanceof Map) {
            Map map = (Map)value;
            if (Map.class.isAssignableFrom(targetClass)) {
                Class clazz = ObjectUtils.getPropertyGenericType(field.getDeclaringClass(), field.getName(), 1);
                return (E)parseMap(map, clazz);
            } else {
                return (E)convertObject((Map<String, Object>) value, targetClass);
            }
        }

        return (E)value;
    }


    protected Map parseMap(Map<Object, Object> map, Class targetClass) {
        Map res = new HashMap<>();

        for (Map.Entry<Object, Object> entry: map.entrySet()) {
            Object value = convertValue(entry.getValue(), targetClass, null);
            res.put(entry.getKey(), value);
        }

        return res;
    }

    protected Collection parseCollection(Collection collection, Class targetClass, Field field) {
        Collection lst = null;
        if (collection instanceof List) {
            lst = new ArrayList<>();
        }

        if (collection instanceof Set) {
            lst = new HashSet<>();
        }

        if (lst == null) {
            throw new ModelException("Collection type not supported: " + collection.getClass());
        }

        Class itemClass = ObjectUtils.getPropertyGenericType(field, 0);

        for (Object value: collection) {
            Object item = convertValue(value, itemClass, null);

            lst.add(item);
        }

        return lst;
    }

    protected JSFunction parseJSFunction(Object value) {
        if (!(value instanceof String)) {
            throw new ModelException("Value must be a string: " + value);
        }

        return new JSFunction((String)value);
    }

    protected JSFuncValue parseJSFuncValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Map) {
            Map map = (Map)value;
            if (map.size() == 1) {
                if (map.containsKey("function")) {
                    return JSFuncValue.function((String)map.get("function"));
                }

                if (map.containsKey("value")) {
                    return JSFuncValue.of(map.get("value"));
                }
            }
        }

        return JSFuncValue.of(value);
    }
}
