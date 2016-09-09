package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.datatable.DataTable;

import java.util.List;

/**
 * Extension of the {@link DataTable} interface that supports
 * an indicator data table, i.e, a data table that supports row and
 * column grouping in several levels
 *
 * @author Ricardo Memoria
 *
 */
public interface DataTableIndicator extends DataTable {

    /**
     * Add a top level row in the table
     * @return instance of the {@link IndicatorRow} class
     */
    IndicatorRow addIndicatorRow();

    /**
     * Add a top level column in the table
     * @return instance of the {@link IndicatorColumn} row
     */
    IndicatorColumn addIndicatorColumn();

    /**
     * Add a row to a parent row
     * @param parent
     * @return
     */
    IndicatorRow addIndicatorRow(IndicatorRow parent);

    /**
     * Add a column as the child of a given column. If no column is
     * specified, the column is included as a top level column
     * @param parent the parent column of the new column to be added, or null if
     * this column has no parent
     * @return instance of the {@link IndicatorColumn} class
     */
    IndicatorColumn addIndicatorColumn(IndicatorColumn parent);

    /**
     * Add an indicator value to the data table
     * @param colkeys are the keys of the indicator columns
     * @param rowkeys are the keys of the indicator rows
     * @param value is the indicator value
     */
    void addIndicatorValue(Object[] colkeys, Object[] rowkeys, double value);

    /**
     * Return the value of a given indicator cell by its column and row index
     * @param colindex is a 0-based index of the column for the indicator values
     * @param rowindex is a 0-based index of the column for the indicator values
     * @return the indicator value, or null if there is no value
     */
    Double getIndicatorValue(int colindex, int rowindex);

    /**
     * Return the size in rows of the column titles
     * @return
     */
    int getHeaderRowsCount();

    /**
     * Return the list of columns in a given row. The row must be in the range
     * of the title columns given by the method <code>getColumnTitleSize()</code>
     * @param row is the 0-based index of the row for the column titles
     * @return instance of the {@link HeaderRow} containing information about the
     * variable of the columns and the list of the columns
     */
    HeaderRow getHeaderRow(int row);

    /**
     * Return information about the title row related to the given row index
     * @param row
     * @return
     */
    IndicatorRow getIndicatorRow(int row);

    /**
     * Return the indicator column of the table by its index. The columns
     * are the end point columns in the table
     * @param colindex is the 0-based index of the column list
     * @return instance of {@link IndicatorColumn} class
     */
    IndicatorColumn getIndicatorColumn(int colindex);

    /**
     * Return the list of rows with information about the titles
     * @return
     */
    List<IndicatorRow> getIndicatorRows();

    /**
     * Return the list of indicator columns of the table, i.e, the list
     * of end point columns
     * @return list of {@link IndicatorColumn} that compound the lowest level
     * of columns
     */
    List<IndicatorColumn> getIndicatorColumns();

    /**
     * Remove an indicator column and all its children columns
     * @param index is the 0-based index position of the column in the indicator table
     */
    void removeIndicatorColumn(int index);

    /**
     * Remove an indicator row and all its child rows
     * @param index is the 0-based index position of the row in the indicator table
     */
    void removeIndicatorRow(int index);
}
