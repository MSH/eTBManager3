package org.msh.etbm.services.cases.filters.impl;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.FilterTypes;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.indicators.variables.VariableOptions;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.db.MessageKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Filter for fields of enum type
 *
 * Created by rmemoria on 17/8/16.
 */
public class EnumFilter extends AbstractFilter {

    private Class enumClass;
    private String fieldName;

    private static final VariableOptions variableOptions = new VariableOptions(false, true, 0,
            new Item<>("cases", "Cases"));

    public EnumFilter(String id, Class enumClass, String label, String fieldName) {
        super(id, label);
        this.fieldName = fieldName;
        this.enumClass = enumClass;
        setVariableOptions(variableOptions);
    }

    @Override
    public String getFilterType() {
        return isMultiSelectionSupported() ?
                FilterTypes.MULTI_SELECT :
                FilterTypes.SELECT;
    }


    @Override
    public void prepareFilterQuery(QueryDefs def, Object value, Map<String, Object> params) {
        addEnumRestrictions(def, value);
    }

    protected void addEnumRestrictions(QueryDefs def, Object value) {
        addValuesRestriction(def, fieldName, value, it -> convertToOrdinal(it));
    }


    /**
     * Convert a string to an enum value
     * @param value the string representing the enum
     * @return
     */
    protected Integer convertToOrdinal(Object value) {
        if (value == null) {
            return null;
        }

        Class<Enum> clazz = getEnumClass();
        if (value.getClass() == getEnumClass()) {
            return ((Enum)value).ordinal();
        }

        Enum[] values = clazz.getEnumConstants();
        for (Enum val: values) {
            if (val.toString().equals(value)) {
                return ((Enum)val).ordinal();
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
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        def.select(fieldName);
    }

    @Override
    public Key createKey(Object[] values, int iteration) {
        if (values[0] == null) {
            return Key.asNull();
        }

        // the object that returns from the table is always an integer
        Enum[] vals = getEnumClass().getEnumConstants();
        int index = (Integer)values[0];

        return Key.of(vals[index]);
    }


    @Override
    public String getKeyDisplay(Key key) {
        if (key.isNull()) {
            return getMessages().get("global.notdef");
        }

        // the object that returns from the table is always an integer
        Enum[] vals = getEnumClass().getEnumConstants();
        Object val = key.getValue();

        Enum e = val instanceof String ? ObjectUtils.stringToEnum((String)val, getEnumClass()) :
                (Enum)val;

        String msgKey = e instanceof MessageKey ? ((MessageKey) e).getMessageKey() :
                enumClass.getSimpleName() + "." + key.toString();
        String txt = getMessages().get(msgKey);
        return txt;
    }

    @Override
    public List<Item> getOptions() {
        Enum[] values = (Enum[])enumClass.getEnumConstants();

        List<Item> options = new ArrayList<>();

        Messages messages = getMessages();

        for (Enum val: values) {
            String key = val instanceof MessageKey ? ((MessageKey) val).getMessageKey() :
                    enumClass.getSimpleName() + "." + val.toString();

            String txt = messages.get(key);

            options.add(new Item(val.toString(), txt));
        }

        return options;
    }

}
