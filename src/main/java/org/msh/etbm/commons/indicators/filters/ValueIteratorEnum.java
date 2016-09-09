package org.msh.etbm.commons.indicators.filters;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Simple filter value that iterates by enumerations. The enumeration are
 * converted from the string value passed to the class
 * Created by rmemoria on 28/5/15.
 */
public abstract class ValueIteratorEnum<E extends Enum> implements ValueHandler.ValueIterator {
    private Class enumClass;

    public abstract String iterateEnum(E value, int index);

    public String iterate(String value, int index) {
        Class clazz = getEnumClass();

        int i = Integer.parseInt(value);
        Enum[] values = (Enum[])clazz.getEnumConstants();

        return iterateEnum((E)values[i], index);
    }

    /**
     * Get declaed enum type
     * @return
     */
    protected Class getEnumClass() {
        if (enumClass != null) {
            return enumClass;
        }

        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            enumClass = (Class) paramType.getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Could not guess entity class by reflection");
        }
        return enumClass;
    }
}
