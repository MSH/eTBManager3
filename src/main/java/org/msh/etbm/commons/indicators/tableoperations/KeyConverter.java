package org.msh.etbm.commons.indicators.tableoperations;

import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.indicators.keys.MultipleKeys;
import org.msh.etbm.commons.indicators.query.IndicatorSqlBuilder;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.*;

/**
 * Converts a data table loaded from a query to another data table with its
 * variables values converted to the corresponding variable keys.
 * <p/>
 * This class is used to support indicator generation. It considers that the last
 * column of the source table contains a numeric value that will be used
 * to generate the values of the indicator
 *
 * @author Ricardo Memoria
 *
 */
public class KeyConverter  {

    private List<Variable> variables;
    private List<int[]> varSourceCols;


    /**
     * Convert the values from the source data table to keys and insert/update them in the destination
     * data table based on the variable configuration in the sql builder object
     * @param source source table containing the raw data from the DB
     * @param dest target table that will receive the keys
     * @param sqlBuilder query builder that generated the source data table with information about the variables
     */
    public void convertKeys(DataTable source, DataTable dest, IndicatorSqlBuilder sqlBuilder) {
        buildVariableList(sqlBuilder);

        // check if destination table has the correct size
        if (dest.getColumnCount() < variables.size() + 1) {
            dest.resize(variables.size() + 1, dest.getRowCount());
        }

        // include values in the destination row
        for (Row row: source.getRows()) {
            Key[] keys = convertRow(row, sqlBuilder);

            // get the last column as being the indicator
            Long val = (Long)row.getValue(source.getColumnCount() - 1);

            updateDestRow(dest, keys, val);
        }
    }

    private void updateDestRow(DataTable dest, Key[] keys, Long value) {
        boolean hasMultipleKeys = false;

        int index = 0;
        for (Key key: keys) {
            if (key instanceof MultipleKeys) {
                hasMultipleKeys = true;
                updateDestRowMultipleKey(dest, keys, value, index);
            }
            index++;
        }

        // if there are multiple keys, just return, because it was called in the iteration above
        if (hasMultipleKeys) {
            return;
        }

        // the position of the keys in the destination table
        int[] pos = new int[keys.length];
        for (int i = 0; i < pos.length; i++) {
            pos[i] = i;
        }

        // get the position of the value in the dest table
        int valIndex = keys.length;

        Row row = dest.findRow(pos, keys, 0);
        if (row == null) {
            // add a new row to the table
            Row newRow = dest.addRow();
            for (int i = 0; i < keys.length; i++) {
                newRow.setValue(i, keys[i]);
            }
            newRow.setValue(valIndex, value);
        } else if (value != null) {
            // update the value in the table incrementing it
            Long currVal = (Long)row.getValue(valIndex);
            currVal = currVal != null ? currVal + value : value;
            row.setValue(keys.length, currVal);
        }
    }

    /**
     * Update the row when a multiple key value is detected in the array of keys
     * @param dest the destination table
     * @param keys the keys
     * @param value the value to update in the dest table
     * @param keyindex the index of the multiple keys in the keys array
     */
    private void updateDestRowMultipleKey(DataTable dest, Key[] keys, Long value, int keyindex) {
        // create a copy of the keys
        Key[] destKeys = Arrays.copyOf(keys, keys.length);

        // get the key with multiple values
        MultipleKeys multiKeys = (MultipleKeys)keys[keyindex];

        // update destination row with normalized key
        for (Key k: multiKeys.getKeys()) {
            destKeys[keyindex] = k;
            updateDestRow(dest, destKeys, value);
        }
    }

    /**
     * Initialize some variables used throughout the class (better performance)
     * @param sqlBuilder
     */
    private void buildVariableList(IndicatorSqlBuilder sqlBuilder) {
        // get the variables involved in the operation
        variables = sqlBuilder.getVariables();
        varSourceCols = new ArrayList<int[]>();

        for (Variable var: variables) {
            varSourceCols.add(sqlBuilder.getColumnsVariable(var));
        }
    }

    /**
     * Convert a row with raw values from the database to an indicator key
     * @param sourceRow the row with values
     * @param sqlBuilder the instance of {@link IndicatorSqlBuilder} containing variable info
     * @return
     */
    private Key[] convertRow(Row sourceRow, IndicatorSqlBuilder sqlBuilder) {
        Key[] keys = new Key[variables.size()];

        int index = 0;
        for (Variable var: variables) {
            int[] cols = varSourceCols.get(index);

            // get the variable values from the source row
            Object[] vals = sourceRow.getValues(cols);

            int iteration = sqlBuilder.getVariableIteration(var);

            keys[index] = var.createKey(vals, iteration);
            if (keys[index] == null) {
                throw new RuntimeException("Cannot return a null key for variable: " + var.getName());
            }
            keys[index].setVariable(var);
            keys[index].setIteration(iteration);
            index++;
        }

        return keys;
    }

}
