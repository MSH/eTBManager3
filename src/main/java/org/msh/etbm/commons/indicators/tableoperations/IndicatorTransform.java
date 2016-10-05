package org.msh.etbm.commons.indicators.tableoperations;

import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTableImpl;
import org.msh.etbm.commons.indicators.indicator.KeyDescriptor;
import org.msh.etbm.commons.indicators.indicator.KeyDescriptorList;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.List;

/**
 * Generates an indicator table from a data table and the variables
 * that generated the data table
 * @author Ricardo Memoria
 *
 */
public class IndicatorTransform {

    /**
     * Generate a new {@link IndicatorDataTable} from the data
     * @param tbl
     * @param columns
     * @param rows
     * @param valueindex
     * @return
     */
    public IndicatorDataTable execute(DataTable tbl, List<Variable> columns, List<Variable> rows, int valueindex) {
        IndicatorDataTableImpl ind = new IndicatorDataTableImpl();

        // calculate key positions
        int[] colsindex = calcVariablePositions(columns, 0);
        int[] rowsindex = calcVariablePositions(rows, colsindex.length);

        // create table descriptors for columns and rows
        createDescriptors(tbl, columns, colsindex, ind.getColumnKeyDescriptors());
        createDescriptors(tbl, rows, rowsindex, ind.getRowKeyDescriptors());

        // fill indicator
        for (Row row: tbl.getRows()) {
            Long val = (Long)row.getValue(valueindex);
            if (val != null) {
                Object[] colkeys = row.getValues(colsindex);
                Object[] rowkeys = row.getValues(rowsindex);

                ind.incValue(colkeys, rowkeys, val.doubleValue());
            }
        }

        // set variable information
        ind.setColumnVariables(columns);
        ind.setRowVariables(rows);

        return ind;
    }

    private int[] calcVariablePositions(List<Variable> vars, int iniPos) {
        int size = 0;
        for (Variable var: vars) {
            size++;
            if (var.getVariableOptions().isGrouped()) {
                size++;
            }
        }

        int[] pos = new int[size];
        for (int i = 0; i < size; i++) {
            pos[i] = i + iniPos;
        }

        return pos;
    }

    /**
     * Mount the list of descriptors for the given dimension (col or row)
     * @param source the source with keys
     * @param variables the list of variables
     * @param cols the position of each variable value in the source
     * @param lst the instance of {@link KeyDescriptorList} to be fed
     */
    protected void createDescriptors(DataTable source, List<Variable> variables, int[] cols, KeyDescriptorList lst) {
        for (int r = 0; r < source.getRowCount(); r++) {
            Object[] vals = source.getRow(r).getValues(cols);
            int index = 0;
            KeyDescriptor parent = null;

            for (Variable var: variables) {
                String s = var.getKeyDisplay(vals[index]);

                KeyDescriptor kd = parent == null ? lst.add(vals[index], s) : parent.add(vals[index], s);
                index++;

                if (var.getVariableOptions().isGrouped()) {
                    String s2 = var.getGroupKeyDisplay(vals[index + 1]);
                    kd = kd.add(vals[index], s);
                    index++;
                }
                parent = kd;
            }
        }
    }

}
