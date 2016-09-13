package org.msh.etbm.commons.indicators.datatable;

import java.util.List;

public interface Row {

    /**
     * Return the value of a given column
     * @param index is the column index starting at 0
     * @return the value of the column at the given index column
     */
    Object getValue(int index);

    /**
     * Set the value of a column of the row
     * @param index is the 0-based position of the column
     * @param value is the value to be set in the column
     */
    void setValue(int index, Object value);

    /**
     * Get a sequence of cell values of the row from a list of column positions
     * @param colindexes is an array containing a sequence of column positions (0-based index)
     * indicating the columns to return its values
     * @return array containing the values of each column
     */
    Object[] getValues(int[] colindexes);

    /**
     * Change the values of a group of columns in the row
     * @param colindexes is an integer array containing the 0-based position of
     * the columns to set values
     * @param values is an array of objects to set the column values
     */
    void setValues(int[] colindexes, Object[] values);

    /**
     * Return the 0-based position of the row in the list of rows
     * @return the index of the row
     */
    int getIndex();

    /**
     * Return the list of values in the row
     * @return list of object values
     */
    List getValues();
}
