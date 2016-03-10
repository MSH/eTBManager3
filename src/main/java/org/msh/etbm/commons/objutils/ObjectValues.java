package org.msh.etbm.commons.objutils;

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
    private Map<String, PropertyValue> values = new HashMap<>();

    /**
     * Extract the values from the object
     * @param obj the object to get values from
     */
    public ObjectValues(Object obj) {
        values.clear();

        Map<String, Object> values = ObjectUtils.describeProperties(obj);
        for (Map.Entry<String, Object> entry: values.entrySet()) {
            this.values.put(entry.getKey(), new PropertyValue(entry.getValue()));
        }
    }


    public ObjectValues() {
        super();
    }

    public PropertyValue get(String prop) {
        return values.get(prop);
    }


    /**
     * Set a value to a given property. If property doesn't exist it will be inlcuded
     * @param prop the property name
     * @param value the value
     */
    public void put(String prop, Object value) {
        values.put(prop, new PropertyValue(value));
    }

    /**
     * Return the list of properties and its values in a map structure
     * @return Map containing property and its values
     */
    public Map<String, PropertyValue> getValues() {
        return values;
    }

}
