package org.msh.etbm.commons.indicators.datatable.impl;

import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;

import java.util.Arrays;
import java.util.List;

public class RowImpl implements Row {

    private Object[] values;
    private Object key;
    private DataTableImpl dataTable;


    public RowImpl(DataTableImpl dataTable) {
        super();
        this.dataTable = dataTable;
        if (dataTable.getColumnCount() > 0) {
            resize(dataTable.getColumnCount());
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Row#getValue(int)
     */
    @Override
    public Object getValue(int index) {
        if (index >= values.length) {
            if (index >= dataTable.getColumnCount()) {
                throw new IndexOutOfBoundsException("Index of column is out of range: " + index);
            }
            return null;
        }
        return values[index];
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Row#setValue(int, java.lang.Object)
     */
    @Override
    public void setValue(int index, Object value) {
        if (values == null) {
            values = new Object[index + 1];
        } else {
            if (index >= values.length) {
                if (index >= dataTable.getColumnCount()) {
                    throw new IndexOutOfBoundsException("Index of column is out of range: " + index);
                }
                resize(index + 1);
            }
        }

        values[index] = value;
    }


    /**
     * Resize the number of columns in the row
     * @param numColumns
     */
    protected void resize(int numColumns) {
        if (values == null) {
            values = new Object[numColumns];
        } else {
            values = Arrays.copyOf(values, numColumns);
        }
    }


    /**
     * Remove the value of a column in the colindex position, reducing the size of the array of values
     * @param colindex
     */
    protected void removeCol(int colindex) {
        if ((colindex < 0) || (colindex >= getColumCount())) {
            throw new IllegalArgumentException("Illegal index " + colindex);
        }

        // shift values to the left, starting at the column to be removed
        int count = getColumCount();
        for (int i = colindex; i < count - 1; i++) {
            values[i] = values[i + 1];
        }

        // resize the values removing the last value
        resize(count - 1);
    }


    /**
     * Insert a new blank column in the row values
     * @param colindex
     */
    protected void insertCol(int colindex) {
        // insert new column at the right side of the table
        int count = getColumCount() + 1;
        resize( count );
        // move values to the right
        for (int i = count - 1; i >= colindex; i-- ) {
            values[i] = values[i - 1];
        }

        // clear the value
        values[colindex] = null;
    }


    /**
     * Return the number of columns in the row
     * @return
     */
    public int getColumCount() {
        return values == null ? 0 : values.length;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Row#getValues()
     */
    @Override
    public List getValues() {
        return values == null ? null : Arrays.asList(values);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Row#getValues(int[])
     */
    @Override
    public Object[] getValues(int[] colindexes) {
        Object[] vals = new Object[colindexes.length];
        int i = 0;
        for (Integer index: colindexes) {
            vals[i++] = getValue(index);
        }
        return vals;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Row#setValues(int[], java.lang.Object[])
     */
    @Override
    public void setValues(int[] colindexes, Object[] values) {
        int i = 0;
        for (Integer colindex: colindexes) {
            this.values[colindex] = values[i++];
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.Row#getIndex()
     */
    @Override
    public int getIndex() {
        return dataTable.getRows().indexOf(this);
    }



    /**
     * @return the dataTable
     */
    public DataTable getDataTable() {
        return dataTable;
    }


    /**
     * @return the key
     */
    public Object getKey() {
        return key;
    }


    /**
     * @param key the key to set
     */
    public void setKey(Object key) {
        this.key = key;
    }


}
