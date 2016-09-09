package org.msh.etbm.commons.indicators.datatable;

/**
 * Simple utility functions used by the data table lib
 */
public class DataTableUtils {


    /**
     * Compare an array of object
     * @param vals1
     * @param vals2
     * @return
     */
    public static int compareArray(Object[] vals1, Object[] vals2) {
        int len = vals1.length;
        if (vals2.length != len) {
            return ((Integer)len).compareTo(vals2.length);
        }

        int res = 0;
        for (int i = 0; i < len; i++) {
            res = compareObjects(vals1[i], vals2[i]);
            if (res != 0) {
                break;
            }
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

        return (obj1 instanceof Comparable) && (obj1.getClass() == obj2.getClass()) ?
                ((Comparable)obj1).compareTo(obj2) :
                obj1.toString().compareTo(obj2.toString());
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
