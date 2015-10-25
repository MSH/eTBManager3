package org.msh.etbm.commons.entities;

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

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }
}
