package org.msh.etbm.commons.indicators.tableoperations;

import org.msh.etbm.commons.indicators.IndicatorException;
import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTableImpl;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates an indicator table from a data table and the variables
 * that generated the data table
 * @author Ricardo Memoria
 *
 */
public class IndicatorTransform {

    private static final String KEY_NULL = "null";

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
                Object[] colkeys = convertKeys(row.getValues(colsindex));
                Object[] rowkeys = convertKeys(row.getValues(rowsindex));

                ind.incValue(colkeys, rowkeys, val.doubleValue());
            }
        }

        // set variable information
        ind.setColumnVariables(columns);
        ind.setRowVariables(rows);

        return ind;
    }

    private Object[] convertKeys(Object[] keys) {
        // calc length of keys in array
        int size = 0;
        for (Object obj: keys) {
            Key key = (Key)obj;
            if (key.getVariable().isGrouped()) {
                size++;
            }
            size++;
        }

        Object[] vals = new Object[size];

        int index = 0;
        for (int i = 0; i < keys.length; i++) {
            Key key = (Key)keys[i];
            if (key.getVariable().isGrouped()) {
                vals[index] = key.getGroup();
                index++;
            }

            Object val = key.getValue();
            vals[index] = val == null ? KEY_NULL : val;
            index++;
        }

        return vals;
    }

    private int[] calcVariablePositions(List<Variable> vars, int iniPos) {
        int size = vars.size();

        int[] pos = new int[size];
        for (int i = 0; i < size; i++) {
            pos[i] = i + iniPos;
        }

        return pos;
    }

    /**
     * Mount the list of descriptors for the given dimension (col or row) containing the title
     * @param source the source with keys
     * @param variables the list of variables
     * @param cols the position of each variable value in the source
     * @param descriptors the descriptors of the indicator
     */
    protected void createDescriptors(DataTable source, List<Variable> variables, int[] cols, List<Map<String, String>> descriptors) {
        // clear descriptors, just in case
        descriptors.clear();

        // search for all keys
        for (int r = 0; r < source.getRowCount(); r++) {
            Object[] vals = source.getRow(r).getValues(cols);
            int index = 0;

            for (Variable var: variables) {
                Key key = (Key)vals[index];
                if (var.isGrouped()) {
                    String s = var.getGroupKeyDisplay(key);
                    if (s == null) {
                        throw new IndicatorException("Invalid key display for value " + vals[index]);
                    }
                    String id = key.getGroup() != null ? key.getGroup().toString() : KEY_NULL;
                    addDescriptor(descriptors, index, id, s);
                    index++;
                }

                String s = var.getKeyDisplay(key);
                String k = key.isNull() ? KEY_NULL : key.getValue().toString();
                addDescriptor(descriptors, index, k, s);

                index++;
            }
        }
    }

    private void addDescriptor(List<Map<String, String>> descriptors, int level, String id, String title) {
        if (level > descriptors.size()) {
            throw new IndicatorException("Must include previous descriptor to the column levels");
        }

        Map<String, String> map;

        if (level == descriptors.size()) {
            map = new HashMap<>();
            descriptors.add(map);
        } else {
            map = descriptors.get(level);
        }

        map.put(id, title);
    }

}
