package org.msh.etbm.commons.indicators.datatable;

import java.util.List;

/**
 * Represent a column of the {@link DataTable} interface
 *
 * @author Ricardo Memoria
 *
 */
public interface Column {

    /**
     * Return the value of a given row
     * @param index is the 0-based position of the row in the column
     * @return the value of the row at the given position
     */
    Object getValue(int index);

    /**
     * Set the value of a row in the column
     * @param index is the 0-based position of the row
     * @param value is the value to be set in the row
     */
    void setValue(int index, Object value);

    /**
     * Get a sequence of cell values of the column from a list of row positions
     * @param rowindexes is an array containing a sequence of row positions (0-based index)
     * indicating the rows to return its values
     * @return array containing the values of each row
     */
    Object[] getValues(int[] rowindexes);

    /**
     * Change the values of a group of rows in the column
     * @param rowindexes is an integer array containing the 0-based position of
     * the rows to set values
     * @param values is an array of objects to set the row values
     */
    void setValues(int[] rowindexes, Object[] values);

    /**
     * Return the 0-based position of the column in the list of columns
     * @return the index of the column
     */
    int getIndex();

    /**
     * Return the list of values of the column
     * @return the index of the column
     */
    List getValues();
}
