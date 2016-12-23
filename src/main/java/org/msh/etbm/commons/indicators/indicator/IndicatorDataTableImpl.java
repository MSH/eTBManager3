package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.IndicatorException;
import org.msh.etbm.commons.indicators.datatable.impl.GroupedDataTableImpl;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of a {@link IndicatorDataTable}
 *
 * Created by rmemoria on 3/10/16.
 */
public class IndicatorDataTableImpl extends GroupedDataTableImpl implements IndicatorDataTable {

    private List<Map<String, String>> columnKeyDescriptors = new ArrayList<>();
    private List<Map<String, String>> rowKeyDescriptors = new ArrayList<>();

    private List<Variable> columnVariables = new ArrayList<>();
    private List<Variable> rowVariables = new ArrayList<>();


    @Override
    public List<Map<String, String>> getColumnKeyDescriptors() {
        return columnKeyDescriptors;
    }

    @Override
    public List<Map<String, String>> getRowKeyDescriptors() {
        return rowKeyDescriptors;
    }

    public void setColumnVariables(List<Variable> columnVariables) {
        this.columnVariables = columnVariables;
    }

    public void setRowVariables(List<Variable> rowVariables) {
        this.rowVariables = rowVariables;
    }

    @Override
    public List<Variable> getColumnVariables() {
        return columnVariables;
    }

    @Override
    public List<Variable> getRowVariables() {
        return rowVariables;
    }

    @Override
    protected int addColumn(int index, Object[] key) {
        int i = key.length - 1;
        // the column descriptor must be included before adding values
        String k = key[i].toString();

        String title = key.length <= columnKeyDescriptors.size() ?
                columnKeyDescriptors.get(i).get(k) :
                null;

        if (title == null) {
            throw new IndicatorException("Column descriptor not found for " + Arrays.toString(key) + " at " + i);
        }
        return super.addColumn(index, key);
    }

    @Override
    protected int addRow(int index, Object[] key) {
        int i = key.length - 1;
        // the row descriptor must be included before adding values
        if (key[i] != null) {
            String k = key[i] != null ? key[i].toString() : "null";
            String title = key.length <= rowKeyDescriptors.size() ?
                    rowKeyDescriptors.get(i).get(k) :
                    null;

            if (title == null) {
                throw new IndicatorException("Row descriptor not found: " + key[i]);
            }
        }
        return super.addRow(index, key);
    }
}
