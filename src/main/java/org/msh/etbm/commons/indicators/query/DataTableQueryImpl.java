package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.datatable.impl.ColumnImpl;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.datatable.impl.RowImpl;

import java.util.AbstractList;
import java.util.List;

/**
 * Implementation of the {@link DataTableQuery} interface
 *
 * @author Ricardo Memoria
 *
 */
public class DataTableQueryImpl extends DataTableImpl implements DataTableQuery {


    @Override
    public String getColumnFieldName(int index) {
        return ((ColumnQuery)getColumn(index)).getFieldName();
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#createColumn()
     */
    @Override
    protected ColumnImpl createColumn() {
        return new ColumnQuery(this);
    }

    @Override
    protected RowImpl createRow() {
        return new RowQuery(this);
    }

    /* (non-Javadoc)
         * @see org.msh.etbm.commons.indicators.query.DataTableQuery#getQueryColumns()
         */
    @Override
    public List<ColumnQuery> getQueryColumns() {
        return new AbstractList<ColumnQuery>() {
            @Override
            public ColumnQuery get(int index) {
                return (ColumnQuery)getColumn(index);
            }

            @Override
            public int size() {
                return getColumns().size();
            }
        };
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.query.DataTableQuery#getQueryRows()
     */
    @Override
    public List<RowQuery> getQueryRows() {
        return new AbstractList<RowQuery>() {
            @Override
            public RowQuery get(int index) {
                return (RowQuery)getRow(index);
            }

            @Override
            public int size() {
                return getRows().size();
            }
        };
    }
}
