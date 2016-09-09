package org.msh.etbm.commons.indicators.datatable;

import org.msh.etbm.commons.indicators.datatable.impl.RowImpl;

import java.util.Comparator;
import java.util.List;


/**
 * Represents a structured data representation in a table format, similar to the
 * records stored in a database table or the result set of a database query, but
 * with the possibility to include or remove rows and columns, even if data is
 * already initialized;
 *
 * @author Ricardo Memoria
 *
 */
public interface DataTable {

    /**
     * Return the number of columns in the data table
     * @return number of columns
     */
    int getColumnCount();

    /**
     * Return the number of rows in the data table
     * @return number of rows
     */
    int getRowCount();


    /**
     * Return the list of the rows in the table
     * @return
     */
    List<Row> getRows();

    /**
     * Return the list of columns if the data table
     * @return
     */
    List<Column> getColumns();

    /**
     * Return the column in the specific position in the list of columns.
     * @param index is the 0-based position of the column in the table
     * @return instance of the {@link Column} interface
     */
    Column getColumn(int index);

    /**
     * Return the row in the specific position in the list of rows.
     * @param index is the 0-based position of the row in the table.
     * @return instance of the {@link Row} interface
     */
    Row getRow(int index);

    /**
     * Return the value of a cell in the data table
     * @param colindex 0-based index of the column
     * @param rowindex 0-based index of the row
     * @return value of the cell of {@link Object} type
     */
    Object getValue(int colindex, int rowindex);

    /**
     * Set the value of a cell in a specified row and column
     * @param colindex 0-based index of the column
     * @param rowindex 0-based index of the row
     * @param value is the new value of the cell in the table
     */
    void setValue(int colindex, int rowindex, Object value);

    /**
     * Insert a new column in the table
     * @param colindex 0-based index of the column that will be inserted
     * @return instance of the {@link Column} interface representing the
     * column inserted
     */
    Column insertColumn(int colindex);

    /**
     * Add a new column in the data table. The column will be included
     * at the end of the column list
     * @return instance of the {@link Column} interface representing the
     * column added
     */
    Column addColumn();

    /**
     * Insert a new row in the data table at the position rowindex. All rows
     * bellow will be shifted down
     * @param rowindex is the 0-based position where the row will be
     * inserted.
     */
    Row insertRow(int rowindex);

    /**
     * Add a new row at the bottom of the table
     * @return instance of the {@link RowImpl} interface representing the new
     * row added
     */
    Row addRow();

    /**
     * Resize the table to the specified number of columns and rows. If
     * the column size or row size is less than the previous values, the
     * table is cropped and the data outside of the new range is lost
     * @param colsize is the number of columns
     * @param rowsize is the number of rows
     */
    void resize(int colsize, int rowsize);

    /**
     * Remove a row of the data table
     * @param rowindex is a 0-based index of a row in the list of rows
     */
    void removeRow(int rowindex);

    /**
     * Remove a column of the data table
     * @param colindex is a 0-based index of the column in the list of columns
     */
    void removeColumn(int colindex);

    /**
     * Search for a column in a range of rows with a specific value
     * @param rowindexes is an int array determining the rows
     * @param values is the values of the rows to search for the column
     * @return instance of the {@link Column} interface or null if the
     * column is not found
     */
    Column findColumn(int[] rowindexes, Object[] values, int colini);

    /**
     * Search for a row in a range of columns with a specific value
     * @param colindexes
     * @param values
     * @return
     */
    Row findRow(int[] colindexes, Object[] values, int rowini);

    /**
     * Swap the position of two rows
     * @param rowindex1 is the 0-based index of the row
     * @param rowindex2 is the 0-based index of the row
     */
    void swapRow(int rowindex1, int rowindex2);

    /**
     * Sort the rows of the data table from a row comparator
     * @param comparator compare two rows of the data table
     */
    void sortRows(Comparator<Row> comparator);
}
