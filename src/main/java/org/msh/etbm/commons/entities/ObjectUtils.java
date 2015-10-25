package org.msh.etbm.commons.entities;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Component with utilities to compare a list of values
 *
 * Created by rmemoria on 25/10/15.
 */
@Component
public class ObjectUtils {

    @Autowired
    DozerBeanMapper mapper;

    /**
     * Generate a list of values from a given object. The object must be mapped in dozer
     * in order to generate a mapping to ObjectValues
     * @param obj
     * @return
     */
    public ObjectValues generateValues(Object obj) {
        return mapper.map(obj, ObjectValues.class);
    }

    /**
     * Compare the properties of two objects and gegerate a diff list
     * @param prevValues the object containing the previous values to compare
     * @param newValues the object containing the new values to compare
     * @return instance of ObjectDiffValues containing the different properties
     */
    public ObjectDiffValues compareOldAndNew(Object prevValues, Object newValues) {
        ObjectValues vals1 = generateValues(prevValues);
        ObjectValues vals2 = generateValues(newValues);

        ObjectDiffValues diffs = new ObjectDiffValues();

        // check properties in the list of previous values
        for (String prop: vals1.getValues().keySet()) {
            checkDiffValues(prop, vals1, vals2, diffs);
        }

        // check new properties in the list of new values
        for (String prop: vals2.getValues().keySet()) {
            if (diffs.get(prop) != null) {
                checkDiffValues(prop, vals1, vals2, diffs);
            }
        }

        return diffs;
    }

    /**
     * Check if values of a given property are different. If so, the values are put in an instance of ObjectDiffValues
     * @param prop the property to check
     * @param vals1 the list of previous object values
     * @param vals2 the list of new object values
     * @param diffs the list store the differences
     * @return true if the values are different
     */
    private boolean checkDiffValues(String prop, ObjectValues vals1, ObjectValues vals2, ObjectDiffValues diffs) {
        Object val1 = vals1.getValues().get(prop);
        Object val2 = vals2.getValues().get(prop);

        if (!isEqualValues(val1, val2)) {
            diffs.put(prop, val1, val2);
            return true;
        }
        return false;
    }

    /**
     * Check if two values are the same
     * @param val1
     * @param val2
     * @return
     */
    public boolean isEqualValues(Object val1, Object val2) {
        if (val1 == val2) {
            return true;
        }

        if (val1 == null || val2 == null) {
            return false;
        }

        return val1.equals(val2);
    }
}
