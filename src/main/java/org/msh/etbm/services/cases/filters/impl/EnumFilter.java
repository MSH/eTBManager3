package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.MessageKey;
import org.msh.etbm.services.cases.filters.FilterContext;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.*;

/**
 * Created by rmemoria on 17/8/16.
 */
public abstract class EnumFilter<E extends Enum> extends AbstractFilter {

    private Class<E> enumClass;

    public EnumFilter(FilterGroup group, String id, String label) {
        super(group, id, label);
    }


    @Override
    public String getFilterType() {
        return isMultiSelectionSupported() ? "multi-select" : "select";
    }

    /**
     * Generate an SQL restriction to be used in the query with the given field and value.
     *
     * @param field the field name
     * @param value The values to filter. It is expected to be a string value or a list of strings
     * @return SQL restriction to be used in the WHERE clause
     */
    protected String sqlRestriction(String field, Object value) {
        StringBuilder s = new StringBuilder();

        if (value instanceof Collection) {
            Collection<String> lst = (Collection)value;

            s.append(field).append(" in ");
            String delim = "(";
            List params = new ArrayList<>();

            for (String item: lst) {
                E val = stringToEnum(item);
                s.append(delim).append(val.ordinal());
            }
        } else {
            E val = stringToEnum((String)value);
            s.append(field).append(" = ").append(val.ordinal());
        }

        return s.toString();
    }

    /**
     * Convert a string to an enum value
     * @param value the string representing the enum
     * @return
     */
    protected E stringToEnum(String value) {
        Class<E> clazz = getEnumClass();
        Enum[] values = clazz.getEnumConstants();
        for (Enum val: values) {
            if (val.toString().equals(value)) {
                return (E)val;
            }
        }
        throw new IllegalArgumentException("Invalid value " + value);
    }

    public Class<E> getEnumClass() {
        if (enumClass == null) {
            enumClass = ObjectUtils.getGenericType(getClass(), 0);
        }
        return enumClass;
    }

    public boolean isRemoteOptions() {
        return false;
    }

    public boolean isMultiSelectionSupported() {
        return true;
    }

    @Override
    public Map<String, Object> getResources(FilterContext context, Map<String, Object> params) {
        if (context.isInitialization() && isRemoteOptions()) {
            return null;
        }

        Class<E> clazz = ObjectUtils.getGenericType(getClass(), 0);

        Enum[] values = clazz.getEnumConstants();

        List<Item> options = new ArrayList<>();

        for (Enum val: values) {
            String key = val instanceof MessageKey ? ((MessageKey) val).getMessageKey() :
                    clazz.getSimpleName() + "." + val.toString();

            String txt = context.getMessages().get(key);

            options.add(new Item(val, txt));
        }

        return Collections.singletonMap("options", options);
    }
}
