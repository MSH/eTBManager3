package org.msh.etbm.commons.entities;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple class to store the values of an object
 * Created by rmemoria on 25/10/15.
 */
public class ObjectValues {

    /**
     * Store the values of the object in a map structure
     */
    private Map<String, Object> values = new HashMap<>();

    public Object get(String prop) {
        return values.get(prop);
    }

    public String getString(String prop) {
        return (String)values.get(prop);
    }


    public Integer getInt(String prop) {
        return (Integer)values.get(prop);
    }

    public Date getDate(String prop) {
        return (Date)values.get(prop);
    }

    public boolean getBool(String prop) {
        Boolean v = (Boolean)values.get(prop);
        return v == null ? false : v;
    }

    public void put(String prop, Object value) {
        values.put(prop, value);
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
