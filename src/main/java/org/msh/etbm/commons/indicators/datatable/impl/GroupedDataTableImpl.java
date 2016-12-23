package org.msh.etbm.commons.indicators.datatable.impl;

import org.msh.etbm.commons.indicators.datatable.DataTableUtils;
import org.msh.etbm.commons.indicators.datatable.GroupedDataTable;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by rmemoria on 2/10/16.
 */
public class GroupedDataTableImpl implements GroupedDataTable {

    /**
     * Table where data is stored
     */
    private DataTableImpl table = new DataTableImpl();

    /**
     * Store the column keys of the grouped table
     */
    private List<Object[]> columnKeys = new ArrayList<>();

    /**
     * Store the row keys of the grouped table
     */
    private List<Object[]> rowKeys = new ArrayList<>();


    @Override
    public Object getValue(Object[] colKeys, Object[] rowKeys) {
        int colindex = findColumn(colKeys);
        if (colindex < 0) {
            return null;
        }

        int rowindex = findRow(rowKeys);
        if (rowindex < 0) {
            return null;
        }

        return table.getValue(colindex, rowindex);
    }

    @Override
    public Object getValueByPosition(int col, int row) {
        return table.getValue(col, row);
    }

    @Override
    public void setValue(Object[] colKeys, Object[] rowKeys, Object value) {
        int colindex = findColumn(colKeys);
        if (colindex < 0) {
            colindex = table.getColumnCount();
            colindex = addColumn(colindex, colKeys);
        }

        int rowindex = findRow(rowKeys);
        if (rowindex < 0) {
            rowindex = table.getRowCount();
            rowindex = addRow(rowindex, rowKeys);
        }

        table.setValue(colindex, rowindex, value);
    }

    @Override
    public void incValue(Object[] colKey, Object[] rowKey, double value) {
        int colindex = findColumn(colKey);
        if (colindex < 0) {
            colindex = table.getColumnCount();
            colindex = addColumn(colindex, colKey);
        }

        int rowindex = findRow(rowKey);
        if (rowindex < 0) {
            rowindex = table.getRowCount();
            rowindex = addRow(rowindex, rowKey);
        }

        Object currValue = table.getValue(colindex, rowindex);
        double newValue = (currValue instanceof Number ? ((Number) currValue).doubleValue() : 0) + value;

        table.setValue(colindex, rowindex, newValue);
    }


    @Override
    public int getColumnCount() {
        return columnKeys.size();
    }

    @Override
    public int getRowCount() {
        return rowKeys.size();
    }

    @Override
    public List<Object[]> getColumnKeys() {
        return columnKeys;
    }

    @Override
    public List<Object[]> getRowKeys() {
        return rowKeys;
    }

    @Override
    public List getRowValues(int index) {
        return table.getRow(index).getValues();
    }

    @Override
    public void removeColumn(Object[] key) {
        int index = findColumn(key);
        columnKeys.remove(index);
        table.removeColumn(index);
    }

    @Override
    public void removeRow(Object[] key) {
        int index = findRow(key);
        rowKeys.remove(index);
        table.removeRow(index);
    }

    /**
     * Search for a column by its key
     * @param keys the column key
     * @return the column index, or (-(insertion point) - 1) if the column was not found
     */
    protected int findColumn(Object[] keys) {
        return findKey(columnKeys, keys);
    }

    /**
     * Search for a row by its key
     * @param keys the row key
     * @return the column index, or (-(insertion point) - 1) if the column was not found
     */
    protected int findRow(Object[] keys) {
        return findKey(rowKeys, keys);
    }

    /**
     * Find a key in a list of keys
     * @param keyList
     * @param keys
     * @return
     */
    private int findKey(List<Object[]> keyList, Object[] keys) {
        int i = 0;
        for (Object[] k: keyList) {
            if (DataTableUtils.compareArray(k, keys) == 0) {
                return i;
            }
            i++;
        }
        return -1;
    }


    /**
     * Add a new column respecting the order of the key among the other keys
     * @param key the key to be included
     */
    protected int addColumn(int index, Object[] key) {
        if (index >= columnKeys.size()) {
            columnKeys.add(key);
            table.addColumn();
            return columnKeys.size() - 1;
        }

        columnKeys.add(index, key);
        table.insertColumn(index);
        return index;
    }

    /**
     * Add a new row respecting the order of the key among the other keys
     * @param key the key to be included
     */
    protected int addRow(int index, Object[] key) {
        if (index >= rowKeys.size()) {
            rowKeys.add(key);
            table.addRow();
            return rowKeys.size() - 1;
        }

        rowKeys.add(index, key);
        table.insertRow(index);
        return index;
    }

}
