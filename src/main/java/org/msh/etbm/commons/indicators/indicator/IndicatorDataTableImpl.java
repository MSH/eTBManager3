package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.IndicatorException;
import org.msh.etbm.commons.indicators.datatable.DataTableFactory;
import org.msh.etbm.commons.indicators.datatable.GroupedDataTable;
import org.msh.etbm.commons.indicators.datatable.impl.GroupedDataTableImpl;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 3/10/16.
 */
public class IndicatorDataTableImpl extends GroupedDataTableImpl implements IndicatorDataTable {

    private KeyDescriptorList columnKeyDescriptors = new KeyDescriptorList();
    private KeyDescriptorList rowKeyDescriptors = new KeyDescriptorList();
    private List<Variable> columnVariables = new ArrayList<>();
    private List<Variable> rowVariables = new ArrayList<>();

    private GroupedDataTable dataTable = DataTableFactory.newGroupedDataTable();

    @Override
    public KeyDescriptorList getColumnKeyDescriptors() {
        return columnKeyDescriptors;
    }

    @Override
    public KeyDescriptorList getRowKeyDescriptors() {
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
        // the column descriptor must be included before adding values
        KeyDescriptor desc = columnKeyDescriptors.findKey(key);
        if (desc == null) {
            throw new IndicatorException("Column descriptor not found");
        }
        return super.addColumn(index, key);
    }

    @Override
    protected int addRow(int index, Object[] key) {
        // the row descriptor must be included before adding values
        KeyDescriptor desc = rowKeyDescriptors.findKey(key);
        if (desc == null) {
            throw new IndicatorException("Row descriptor not found");
        }
        return super.addRow(index, key);
    }
}
