package org.msh.etbm.commons.indicators.filters;

/**
 * Represents a filter value sent from the server, with specialized method
 * to handle different treatments to the value
 *
 * Created by rmemoria on 28/5/15.
 */
public class ValueHandler {

    private String value;
    private boolean multiselection;

    /**
     * Interface that must be implemented for any variable that want to iterate
     * through the values
     */
    public interface ValueIterator {
        String iterate(String value, int index);
    }

    /**
     * Default constructor, passing the server value as reference
     * @param value the filter value sent from the server
     * @param multiselection true if value may be represented in an array separated by semi-comma
     */
    public ValueHandler(String value, boolean multiselection) {
        this.value = value;
        this.multiselection = multiselection;
    }

    /**
     * Return the filter value as it was sent from the server
     * @return
     */
    public String asString() {
        return value;
    }

    /**
     * Return the filter value converted to integer
     * @return
     */
    public Integer asInteger() {
        return Integer.parseInt(value);
    }

    /**
     * Check if the value represents an array of values
     * @return
     */
    public boolean isArray() {
        return multiselection && value.indexOf(";") >= 0;
    }


    /**
     * Return the list of elements in the value (only available if elements
     * are separated by semi-comma)
     * @return array of string values
     */
    public String[] getElements() {
        if (value == null) {
            return new String[0];
        }

        if (multiselection) {
            return value.split(";");
        }

        String[] vals = new String[1];
        vals[0] = value;
        return vals;
    }


    /**
     * Iterate through the values (or the value, if it's not an array)
     * using the iterator passed as argument
     * @param iterator
     * @return
     */
    public String[] map(ValueIterator iterator) {
        if (iterator == null) {
            return getElements();
        }

        String[] res;
        if (isArray()) {
            String[] elems = getElements();
            res = new String[elems.length];
            for (int i = 0; i < elems.length; i++) {
                res[i] = iterator.iterate(elems[i], i);
            }
        } else {
            res = new String[1];
            res[0] = iterator.iterate(this.value, 0);
        }

        return res;
    }

    /**
     * Iterate through the values and create an SQL IN declaration with
     * the values returned from the iterator
     * @param iterator the iterator to pass values through
     * @return SQL IN declaration
     */
    public String mapSqlIN(ValueIterator iterator) {
        String[] sqls = map(iterator);

        if (sqls.length == 1) {
            return " = " + sqls[0];
        }

        String sql = "";
        for (String s: sqls) {
            if (!sql.isEmpty()) {
                sql += ",";
            }
            sql += s;
        }
        return " in (" + sql + ")";
    }

    /**
     * Iterate through the values and create an SQL OR with the values
     * returned from the iterator
     * @param iterator the iterator to pass values through
     * @return SQL OR declaration
     */
    public String mapSqlOR(ValueIterator iterator) {
        String[] sqls = map(iterator);
        String sql = "";
        for (String s: sqls) {
            if (!sql.isEmpty()) {
                sql += " or ";
            }
            sql += "(" + s + ")";
        }
        return "(" + sql + ")";
    }
}
