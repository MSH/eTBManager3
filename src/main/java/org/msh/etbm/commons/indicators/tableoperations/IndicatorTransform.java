package org.msh.etbm.commons.indicators.tableoperations;

import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.indicator.*;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates an indicator table from a data table and the variables
 * that generated the data table
 * @author Ricardo Memoria
 *
 */
public class IndicatorTransform {

    private List<Variable> varRows = new ArrayList<Variable>();
    private List<Variable> varCols = new ArrayList<Variable>();

    private DataTableIndicator tblind;

    public DataTableIndicator excute(DataTable tbl, List<Variable> columns, List<Variable> rows, int valueindex) {
        tblind = new DataTableIndicatorImpl();

        // calculate number of keys for columns and rows
        varRows.clear();
        varCols.clear();

        for (Variable var: columns) {
            if (var.isGrouped()) {
                varCols.add(var);
            }
            varCols.add(var);
        }

        for (Variable var: rows) {
            if (var.isGrouped()) {
                varRows.add(var);
            }
            varRows.add(var);
        }

        // calculate key positions
        int index = 0;
        int[] colsindex = new int[varCols.size()];
        int[] rowsindex = new int[varRows.size()];

        for (int i = 0; i < varCols.size(); i++) {
            colsindex[i] = index++;
        }

        for (int i = 0; i < varRows.size(); i++) {
            rowsindex[i] = index++;
        }

        // fill indicator
        for (Row row: tbl.getRows()) {
            Long val = (Long)row.getValue(valueindex);
            if (val != null) {
                Object[] colkeys = row.getValues(colsindex);
                Object[] rowkeys = row.getValues(rowsindex);

                tblind.addIndicatorValue(colkeys, rowkeys, val.doubleValue());
            }
        }

        updateVariableInformation();

        return tblind;
    }

    /**
     * Update information of the variables in rows and columns
     */
    protected void updateVariableInformation() {
        // set the variable of the rows
        for (IndicatorRow row: tblind.getIndicatorRows()) {
            Object key = row.getKey();
            Variable var = varRows.get(row.getLevel());
            row.setVariable( var );

            // update title of the rows
            // check if current row is grouped
            boolean isGrouped = (var.isGrouped() && ((row.getParent() == null) || (row.getParent().getVariable() != var)));
            if (isGrouped) {
                row.setTitle(var.getGroupDisplayText(key));
            } else {
                row.setTitle(var.getDisplayText(key));
            }
        }

        // set the variable of the columns
        Variable prevvar = null;
        for (int i = 0; i < tblind.getHeaderRowsCount(); i++) {
            HeaderRow row = tblind.getHeaderRow(i);
            Variable var = varCols.get(i);
            row.setVariable( var );

            // update title of the columns
            boolean isGrouped = (var.isGrouped() && (prevvar != var));
            if (isGrouped) {
                for (IndicatorColumn col: row.getColumns()) {
                    col.setTitle(var.getGroupDisplayText(col.getKey()));
                }
            } else {
                for (IndicatorColumn col : row.getColumns()) {
                    col.setTitle(var.getDisplayText(col.getKey()));
                }
            }
            prevvar = var;
        }
    }
}
