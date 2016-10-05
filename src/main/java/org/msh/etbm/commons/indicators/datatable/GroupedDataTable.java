package org.msh.etbm.commons.indicators.datatable;

import java.util.List;

/**
 * Describe the methods of a table that store values in grouped keys.
 * The main difference between a {@link DataTable} is that columns are
 * included based on the key values. If a key doesn't exist, a new one
 * is included. Columns and rows are ordered according to its keys, so
 * once a new key is included, it will be included in its ordered position
 * <p/>
 * Keys are included as an array, so a column or row may be represented by
 * a key array.
 *
 * Created by rmemoria on 2/10/16.
 */
public interface GroupedDataTable {

    /**
     * Get the value of a cell by its keys. If the cell does not exist, the
     * return will be null
     * @param colKeys the keys of the column
     * @param rowKeys the keys of the row
     * @return the value in the cell, or null if the cell doesn't exist
     */
    Object getValue(Object[] colKeys, Object[] rowKeys);

    /**
     * Set the value of a cell by its keys. If the cell exists, the value is replaced,
     * otherwise, a new column and/or row will be created and the value set
     * @param colKeys the keys that represent the column
     * @param rowKeys the keys that represent the row
     * @param value the value to be set
     */
    void setValue(Object[] colKeys, Object[] rowKeys, Object value);

    /**
     * Increment the value of a cell that contains a number. If cell doesn't contain a
     * number, its value is reset to the given value
     * @param colKey the key of the column
     * @param rowKey the key of the row
     * @param value the value to be incremented
     */
    void incValue(Object[] colKey, Object[] rowKey, double value);

    /**
     * Return the number of columns in the table
     * @return int value
     */
    int getColumnCount();

    /**
     * Return the number of rows in the table
     * @return int value
     */
    int getRowCount();

    /**
     * Return all keys representing the columns of the table
     * @return
     */
    List<Object[]> getColumnKeys();

    /**
     * Return all keys representing the rows of the table
     * @return
     */
    List<Object[]> getRowKeys();

    /**
     * Return all row values for the given row index. Used as a fast way of getting
     * the values from the table
     * @param index the zero-based row index
     * @return the list of values from the row, including null values
     */
    List getRowValues(int index);

    /**
     * Remove a column of the table by its key
     * @param key the key of the column to remove
     */
    void removeColumn(Object[] key);

    /**
     * Remove a row of the table by its key
     * @param key the key of the row to remove
     */
    void removeRow(Object[] key);
}
