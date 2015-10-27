package org.msh.etbm.commons.commands.data;

import org.msh.etbm.commons.entities.ObjectValues;

import java.util.HashMap;
import java.util.Map;

/**
 * Store a list of values in the log data
 * Created by rmemoria on 17/10/15.
 */
public class ListLogData implements CommandData {

    private Map<String, Object> values = new HashMap<>();

    public void put(String item, Object value) {
        values.put(item, value);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

    public ListLogData add(String key, Object value) {
        values.put(key, value);
        return this;
    }

    @Override
    public DataType getType() {
        return DataType.LIST;
    }

    @Override
    public Object getDataToSerialize() {
        return values;
    }

    public ListLogData put(ObjectValues vals) {
        for (String prop: vals.getValues().keySet()) {
            put(prop, vals.getValues().get(prop));
        }
        return this;
    }
}
