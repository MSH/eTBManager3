package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.datatable.impl.RowImpl;

public class RowQuery extends RowImpl {

    public RowQuery(DataTableImpl dataTable) {
        super(dataTable);
    }

    /**
     * @param fieldName
     * @return
     */
    public Object getValue(String fieldName) {
        for (ColumnQuery col: ((DataTableQuery)getDataTable()).getQueryColumns()) {
            if (col.getFieldName().equals(fieldName)) {
                return getValue(col.getIndex());
            }
        }
        throw new IllegalArgumentException("Field name not found: " + fieldName);
    }
}
