package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.datatable.DataTable;

import java.util.List;

/**
 * Standard implementation of a data table that contains
 * data from a query
 * @author Ricardo Memoria
 *
 */
public interface DataTableQuery extends DataTable {

    /**
     * Return the field name of a column
     * @param index is the zero-based column index
     * @return String value containing the column field name
     */
    public String getColumnFieldName(int index);

    /**
     * Return the columns of the data table query
     * @return {@link List} of {@link ColumnQuery} objects
     */
    List<ColumnQuery> getQueryColumns();

    /**
     * Return the rows of the data table query
     * @return {@link List} of {@link RowQuery} objects
     */
    List<RowQuery> getQueryRows();

}
