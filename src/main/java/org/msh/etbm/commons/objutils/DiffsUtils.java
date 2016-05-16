package org.msh.etbm.commons.objutils;

import java.util.ArrayList;
import java.util.List;

/**
 * Compare objects, generate differences between them
 *
 * Created by rmemoria on 28/1/16.
 */
public class DiffsUtils {

    private DiffsUtils() {
        super();
    }

    /**
     * Generate a list of object values. The list of object values can be used to compare to other values
     * @param obj
     * @return
     */
    public static ObjectValues generateValues(Object obj) {
        return new ObjectValues(obj);
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
     * Compare the properties of two objects and generate a diff list
     * @param prevValues the object containing the previous values to compare
     * @param newValues the object containing the new values to compare
     * @return instance of ObjectDiffValues containing the different properties
     */
    public static Diffs generateDiffs(Object prevValues, Object newValues) {
        ObjectValues vals1 = new ObjectValues(prevValues);
        ObjectValues vals2 = new ObjectValues(newValues);

        return generateDiffsFromValues(vals1, vals2);
    }

    /**
     * Generate a list of different properties based on the property values
     * @param vals1 list of properties and its values of first object to compare to
     * @param vals2 list of properties and its values of second object to compare to
     * @return instance of {@link Diffs} containing the differences
     */
    public static Diffs generateDiffsFromValues(ObjectValues vals1, ObjectValues vals2) {
        Diffs diffs = new Diffs();

        // check properties in the list of previous values
        for (String prop: vals1.getValues().keySet()) {
            checkDiffValues(prop, vals1, vals2, diffs);
        }

        // check new properties in the list of new values
        for (String prop: vals2.getValues().keySet()) {
            if (vals1.get(prop) == null) {
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
        PropertyValue pv1 = vals1.getValues().get(prop);
        PropertyValue pv2 = vals2.getValues().get(prop);

        if (pv1.isCollection()) {
            return handleCollectionDiffs(diffs, prop, pv1, pv2);
        }

        if (!compareEquals(pv1.get(), pv2.get())) {
            diffs.put(prop, pv1.get(), pv2.get());
            return true;
        }

        return false;
    }

    /**
     * Collect changes in a collection
     * @param diffs
     * @param prop
     * @param p1
     * @param p2
     */
    private static boolean handleCollectionDiffs(Diffs diffs, String prop, PropertyValue p1, PropertyValue p2) {
        List addedList = new ArrayList<>();
        List changedList = new ArrayList<>();
        List removedList = new ArrayList<>();

        boolean diff = false;

        // check the added items and start mounting the changed items
        for (CollectionItem item: p2.getItems()) {
            CollectionItem prevItem = p1.findItemByValue(item.getValue());

            // item doesn't exist in the previous list, so it is a new item
            if (prevItem == null) {
                addedList.add(item.getValue());
                diff = true;
            } else if (prevItem.getHash() != item.getHash()) {
                changedList.add(item.getValue());
                diff = true;
            }
        }

        // mount removed list and complete the changed items
        for (CollectionItem item: p1.getItems()) {
            CollectionItem newItem = p2.findItemByValue(item.getValue());

            // item was not found in the new list ?
            if (newItem == null) {
                // so include it in the list of removed items
                removedList.add(item.getValue());
                diff = true;
            } else if (item.getHash() != newItem.getHash() && !changedList.contains(newItem.getValue())) {
                // if the content is different and item is not already in the changed list, include it
                changedList.add(newItem.getValue());
                diff = true;
            }
        }

        // values are different ?
        if (diff) {
            // add the changes in the collection
            diffs.putCollection(prop,
                    addedList.size() > 0 ? addedList : null,
                    removedList.size() > 0 ? removedList : null,
                    changedList.size() > 0 ? changedList : null);
        }

        return diff;
    }
}
