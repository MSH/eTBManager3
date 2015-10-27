package org.msh.etbm.commons.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Store a list of object property values with its previous value and its new value.
 * If in a specific property, previous and new values are the same, the value is not supposed to be stored
 *
 * Created by rmemoria on 25/10/15.
 */
public class Diffs {

    /**
     * Store the previous and new value of object properties
     */
    private Map<String, DiffValue> values = new HashMap<>();

    /**
     * Add a new object difference to the list
     * @param prop
     * @param prevValue
     * @param newValue
     */
    public void put(String prop, Object prevValue, Object newValue) {
        DiffValue d = new DiffValue(prevValue, newValue);
        values.put(prop, d);
    }

    /**
     * Return the difference of values in a property, if exists
     * @param prop the property name
     * @return instance of DiffValue, or null if property is not found
     */
    public DiffValue get(String prop) {
        return values.get(prop);
    }

    public Map<String, DiffValue> getValues() {
        return values;
    }

    public void setValues(Map<String, DiffValue> values) {
        this.values = values;
    }
}
