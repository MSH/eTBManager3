package org.msh.etbm.commons.objutils;

import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.commons.entities.Diffs;
import org.msh.etbm.commons.entities.ObjectValues;

import java.beans.PropertyDescriptor;
import java.util.Map;

/**
 * Compare objects, generate differences between them
 *
 * Created by rmemoria on 28/1/16.
 */
public class ObjectDiffs {

    private ObjectDiffs() {
        super();
    }

    /**
     * Generate a list of values from a given object. The object must be mapped in dozer
     * in order to generate a mapping to ObjectValues
     * @param obj
     * @return
     */
    public static ObjectValues generateValues(Object obj) {
        Map<String, Object> values;
        try {
            values = PropertyUtils.describe(obj);

            PropertyDescriptor[] props = PropertyUtils.getPropertyDescriptors(obj);

            // remove properties that don't have a get or a set
            for (PropertyDescriptor prop: props) {
                if (prop.getReadMethod() == null || prop.getWriteMethod() == null) {
                    values.remove(prop.getName());
                }
            }

        } catch (Exception e) {
            throw new ObjectAccessException("Error getting object properties", e);
        }

        ObjectValues vals = new ObjectValues();
        vals.setValues(values);
        return vals;
    }

    /**
     * Compare if objects are the same. It checks if references point to the same object.
     * If not, uses the equals method to compare them
     * @param val1 the first object to compare
     * @param val2 the second object to compare
     * @return true if objects are the same, otherwise false
     */
    public static boolean compareEquals(Object val1, Object val2) {
        if (val1 == val2) {
            return true;
        }

        if (val1 == null || val2 == null) {
            return false;
        }

        return val1.equals(val2);
    }

    /**
     * Compare the properties of two objects and gegerate a diff list
     * @param prevValues the object containing the previous values to compare
     * @param newValues the object containing the new values to compare
     * @return instance of ObjectDiffValues containing the different properties
     */
    public static Diffs compareOldAndNew(Object prevValues, Object newValues) {
        ObjectValues vals1 = generateValues(prevValues);
        ObjectValues vals2 = generateValues(newValues);

        Diffs diffs = new Diffs();

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
    private static boolean checkDiffValues(String prop, ObjectValues vals1, ObjectValues vals2, Diffs diffs) {
        Object val1 = vals1.getValues().get(prop);
        Object val2 = vals2.getValues().get(prop);

        if (!compareEquals(val1, val2)) {
            diffs.put(prop, val1, val2);
            return true;
        }
        return false;
    }

}
