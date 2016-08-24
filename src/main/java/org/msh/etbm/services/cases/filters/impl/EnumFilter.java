package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.MessageKey;
import org.msh.etbm.services.cases.filters.FilterContext;
import org.msh.etbm.services.cases.filters.FilterGroup;

import java.util.*;

/**
 * Created by rmemoria on 17/8/16.
 */
public class EnumFilter extends AbstractFilter {

    private Class enumClass;
    private String fieldName;

    public EnumFilter(FilterGroup group, Class enumClass, String id, String label, String fieldName) {
        super(group, id, label);
        this.fieldName = fieldName;
        this.enumClass = enumClass;
    }

    @Override
    public String getFilterType() {
        return isMultiSelectionSupported() ? "multi-select" : "select";
    }


    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        String s = sqlRestriction(fieldName, value);
        def.restrict(s);
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

        Collection<String> lst = value instanceof Collection ? (Collection)value : null;

        // check if there is one single value
        Object singleValue;
        if (lst == null) {
            singleValue = value;
        } else {
            singleValue = lst.size() == 1 ? lst.toArray()[0] : null;
        }

        // handle collection
        if (lst != null && lst.size() > 1) {
            s.append(field).append(" in ");
            String delim = "(";
            List params = new ArrayList<>();

            for (String item: lst) {
                Enum val = stringToEnum(item);
                s.append(delim).append(val.ordinal());
            }
            s.append(")");
        } else {
            Enum val = stringToEnum((String)singleValue);
            s.append(field).append(" = ").append(val.ordinal());
        }

        return s.toString();
    }

    /**
     * Convert a string to an enum value
     * @param value the string representing the enum
     * @return
     */
    protected Enum stringToEnum(String value) {
        Class<Enum> clazz = getEnumClass();
        Enum[] values = clazz.getEnumConstants();
        for (Enum val: values) {
            if (val.toString().equals(value)) {
                return (Enum)val;
            }
        }
        throw new IllegalArgumentException("Invalid value " + value);
    }

    public Class<Enum> getEnumClass() {
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

        Enum[] values = (Enum[])enumClass.getEnumConstants();

        List<Item> options = new ArrayList<>();

        for (Enum val: values) {
            String key = val instanceof MessageKey ? ((MessageKey) val).getMessageKey() :
                    enumClass.getSimpleName() + "." + val.toString();

            String txt = context.getMessages().get(key);

            options.add(new Item(val, txt));
        }

        return Collections.singletonMap("options", options);
    }
}
