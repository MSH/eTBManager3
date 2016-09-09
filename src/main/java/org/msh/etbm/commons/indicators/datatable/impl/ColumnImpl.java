package org.msh.etbm.commons.indicators.datatable.impl;

import org.msh.etbm.commons.indicators.datatable.Column;
import org.msh.etbm.commons.indicators.datatable.Row;

public class ColumnImpl implements Column {

    private DataTableImpl dataTable;

    public ColumnImpl(DataTableImpl dataTable) {
        super();
        this.dataTable = dataTable;
    }

    /**
     * @return the dataTable
     */
    public DataTableImpl getDataTable() {
        return dataTable;
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Column#getIndex()
     */
    @Override
    public int getIndex() {
        return dataTable.getColumns().indexOf(this);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Column#getValue(int)
     */
    @Override
    public Object getValue(int index) {
        Row row = dataTable.getRow(index);
        return row.getValue(getIndex());
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Column#setValue(int, java.lang.Object)
     */
    @Override
    public void setValue(int index, Object value) {
        Row row = dataTable.getRow(index);
        row.setValue(getIndex(), value);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Column#getValues(int[])
     */
    @Override
    public Object[] getValues(int[] rowindexes) {
        Object[] vals = new Object[rowindexes.length];
        for (int i = 0; i < rowindexes.length; i++) {
            vals[i] = getValue(rowindexes[i]);
        }
        return vals;
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Column#setValues(int[], java.lang.Object[])
     */
    @Override
    public void setValues(int[] rowindexes, Object[] values) {
        for (int i = 0; i < rowindexes.length; i++) {
            setValue(rowindexes[i], values[i]);
        }
    }
}
