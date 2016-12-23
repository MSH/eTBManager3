package org.msh.etbm.commons.indicators.datatable;

/**
 * Simple utility functions used by the data table lib
 */
public class DataTableUtils {

    public static final String TOTAL = "total";

    // total that is included in the left side of a group key
    public static final String LG_TOTAL = "lgtotal";

    // total that is included in the right side of a group key
    public static final String RG_TOTAL = "rgtotal";


    /**
     * Compare an array of object. Return -1 if vals1 is before vals2. Return 0 if both arrays are equals,
     * i.e, if all elements in the correspondent index position of both arrays are the same, otherwise
     * return 1 if vals2 is before vals1
     * @param vals1 the array to be compared to vals2
     * @param vals2 the array to be compared to vals1
     * @return -1, 0 or 1
     */
    public static int compareArray(Object[] vals1, Object[] vals2) {
        int len = vals1.length < vals2.length ? vals1.length : vals2.length;

        int res = 0;
        for (int i = 0; i < len; i++) {
            res = compareObjects(vals1[i], vals2[i]);
            if (res != 0) {
                break;
            }
        }

        // check if the result is equal but array sizes are different
        if (res == 0 && vals1.length != vals2.length) {
            return Integer.compare(vals1.length, vals2.length);
        }

        return res;
    }


    /**
     * Compare two objects. The objects are considered of the same type. It uses the {@link Comparable} interface
     * if the first object implements it, otherwise they are converted to string and compared
     * @param obj1 The object value to be compared to the second method argument
     * @param obj2 The object value to be compared to the first method argument
     * @return
     */
    protected static int compareObjects(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return 0;
        }

        if (obj1 == null) {
            return -1;
        }

        if (obj2 == null) {
            return 1;
        }

        // exclusive for table comparision
        if (TOTAL.equals(obj1) || RG_TOTAL.equals(obj1) || LG_TOTAL.equals(obj2)) {
            return -1;
        }

        if (TOTAL.equals(obj2) || RG_TOTAL.equals(obj2) || LG_TOTAL.equals(obj1)) {
            return 1;
        }

        int res = (obj1 instanceof Comparable) && (obj1.getClass() == obj2.getClass()) ?
                ((Comparable)obj1).compareTo(obj2) :
                obj1.toString().compareTo(obj2.toString());

        if (res < 0) {
            return -1;
        }

        return res > 0 ? 1 : 0;
    }

    /**
     * Check if two objects are equals. If they are not equals testing the equals operator,
     * a test using the <code>equals()</code> is applied. It also checks null pointers.
     *
     * @param val1 object to be compared
     * @param val2 object to be compared
     * @return true if they are the same
     */
    public static boolean equalValue(Object val1, Object val2) {
        if (val1 == val2) {
            return true;
        }

        if ((val1 == null) || (val2 == null)) {
            return false;
        }

        return (val1.equals(val2));
    }
}
