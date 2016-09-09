package org.msh.etbm.commons.indicators.tableoperations;

import org.msh.etbm.commons.indicators.datatable.Column;
import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.DataTableUtils;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.indicator.DataTableIndicator;
import org.msh.etbm.commons.indicators.indicator.DataTableIndicatorImpl;
import org.msh.etbm.commons.indicators.indicator.IndicatorRow;

import java.util.*;


/**
 * Execute a data table transformation generating a new table in cube format from a
 * table indicating the columns that will be transformed in rows and columns in the
 * new table.
 * <p/>
 * This class expects a table with a column to be used as the value of the new
 * data table cells. This value is expected to be of type long and, if necessary,
 * it'll be summed with other cell values
 *
 * @author Ricardo Memoria
 *
 */
public class CubeTransform {

    /**
     * The data table containing the source of data
     */
    private DataTable sourcedt;

    /**
     * The new data table being created
     */
    private DataTableIndicatorImpl newdt;

    private int[] newrows;
    private int[] newcols;

    /**
     * Indexes of the columns in the new data table
     */
    int[] colsindex;

    /**
     * The column in the source table where the indicator value is
     */
    private int valuecol;

    private Comparator<RowValues> comparator;
    // group the rows in a single row by key (single or composed)
    private boolean rowGroupping;

    private List<Object[]> sortedRows;

    /**
     * List of keys and its rows in the source table
     */
    private Map<Object[], List<Row>> rowsByKey;

    /**
     * Transform a data table into a cubic data table.
     *
     * @param sourcedt is the instance of {@link DataTableImpl} with columns and source of data
     * @param newrows
     * @param newcols
     * @return
     */
    public DataTableIndicator transform(DataTable sourcedt, int[] newcols, int[] newrows, int colvalue) {
        this.newrows = newrows;
        this.newcols = newcols;
        this.sourcedt = sourcedt;
        this.valuecol = colvalue;

        newdt = new DataTableIndicatorImpl();
        for (Row row: sourcedt.getRows()) {
            Long val = (Long)row.getValue(colvalue);
            if (val != null) {
                Object[] colkeys = row.getValues(newcols);
                Object[] rowkeys = row.getValues(newrows);

                newdt.addIndicatorValue(colkeys, rowkeys, val.doubleValue());
            }
        }

        return newdt;
    }



    /**
     * Sort rows of the data table
     * @return
     */
    protected List<Object[]> sortRows() {
        List<Object[]> rows = new ArrayList<Object[]>();
        rowsByKey = new HashMap<Object[], List<Row>>();

        for (Row row: sourcedt.getRows()) {
            Object[] vals = row.getValues(newrows);

            // search for item in the list
            Object[] item = null;
            for (Object[] aux: rows) {
                if (Arrays.equals(aux, vals)) {
                    item = aux;
                    break;
                }
            }

            // if key is not in the list, so include it
            if (item == null) {
                item = vals;
                rows.add(vals);
            }

            // include the row in the list of the key
            List<Row> keyRows = rowsByKey.get(item);
            if (keyRows == null) {
                keyRows = new ArrayList<Row>();
                rowsByKey.put(vals, keyRows);
            }
            keyRows.add(row);
        }

        // sort row values
        Collections.sort(rows, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                return DataTableUtils.compareArray(o1, o2);
            }
        });

        return rows;
    }


    /**
     * Create the rows of the new data table for row grouping, i.e, initialize the rows
     * when the option rowGrouping is true
     *
     */
    protected void createRowsList() {
        // these are the row indexes of the title columns
        colsindex = new int[newcols.length];
        for (int i = 0; i < colsindex.length; i++) {
            colsindex[i] = i;
        }

        // create new rows (just with the keys) using a list format
        createRowsFromKeys(null, sortedRows, 0, 0);
    }


    /**
     * Create rows in the given table from a list of keys. The rows are created in a hierarchical order
     * based on the keys available in the list. This method is called recursively inside the class
     * to generate the rows of the new table in a list format (i.e, not grouping rows in columns)
     * @param parentRow
     * @param lst
     * @param keyindex
     * @param index
     * @return
     */
    private int createRowsFromKeys(IndicatorRow parentRow, List<Object[]> lst, int keyindex, int index) {
        Object[] keys = lst.get(index);

        /// get parent key
        Object pk = keyindex > 0 ? keys[keyindex - 1] : null;

        while (true) {
            IndicatorRow row = parentRow == null ?
                    newdt.addIndicatorRow() :
                    newdt.addIndicatorRow(parentRow);

            row.setValue(0, keys[keyindex]);

            // is NOT the last key in the array, i.e, is not the end point of the row hierarchy ?
            if (keyindex < keys.length - 1) {
                index = createRowsFromKeys(row, lst, keyindex + 1, index);
            } else {
                updateRowValues(row, keys);
                index++;
            }

            // is last key ?
            if (index >= lst.size()) {
                return index;
            }
            keys = lst.get(index);

            // get the current new parent key
            Object newpk = keyindex > 0 ? keys[keyindex - 1] : null;

            // parent has changed ?
            if (!DataTableUtils.equalValue(pk, newpk)) {
                return index;
            }
        }
    }

    /**
     * Update the row and its parent values according to its key in the source table.
     * This method is used in a list grouping, where parent rows are available
     *
     * @param row
     * @param keys
     */
    private void updateRowValues(Row row, Object[] keys) {
        // get all rows with the specific key
        List<Row> sourcerows = rowsByKey.get(keys);

        for (Row sourcerow: sourcerows) {
            Long value = (Long)sourcerow.getValue(valuecol);
            Object[] col = sourcerow.getValues(newcols);
            Column c = newdt.findColumn(colsindex, col, 1);

            Row r = row;
            int cindex = c.getIndex();
        }
    }



    /**
     * Create the rows of the cube data table by grouping the rows key in separate columns
     */
    protected void createRowsByKeyColumns() {
        // create rows
        int[] rowsindex = new int[newrows.length];
        colsindex = new int[newcols.length];

        for (int i = 0; i < rowsindex.length; i++) {
            rowsindex[i] = i;
        }

        for (int i = 0; i < colsindex.length; i++) {
            colsindex[i] = i;
        }

        // create new rows
        for (Object[] vals: sortedRows) {
            Row row = newdt.addRow();
            row.setValues(rowsindex, vals);
        }

        // fill values in the rows
        for (Row row: sourcedt.getRows()) {
            Object[] vals = row.getValues(newcols);

            Object[] rvals = row.getValues(newrows);
            Row nr = newdt.findRow(rowsindex, rvals, newcols.length);
            if (nr == null) {
                nr = newdt.addRow();
                nr.setValues(rowsindex, rvals);
            }

            Column c = newdt.findColumn(colsindex, vals, newrows.length);

            newdt.setValue(c.getIndex(), nr.getIndex(), row.getValue(valuecol));
        }
    }

    /**
     * Create a list of unique values from a set of columns in a {@link DataTableImpl} instance
     * @param dt
     * @param cols
     * @return
     */
    protected List<Object[]> createKeyList(DataTable dt, int[] cols) {
        int len = cols.length;

        List<Object[]> lst = new ArrayList<Object[]>();

        for (Row row: dt.getRows()) {
            Object[] vals = new Object[len];
            int index = 0;
            for (Integer colindex: cols) {
                vals[index] = row.getValue(colindex);
                index++;
            }

            // check if item is already in the list
            index = itemIndex(lst, vals);

            if (index == -1) {
                lst.add(vals);
            }
        }

        return lst;
    }


    /**
     * Find item inside the list
     * @param lst list of items to be scanned
     * @param vals item of the list to be searched
     * @return index of the list
     */
    private int itemIndex(List<Object[]> lst, Object[] vals) {
        int index = 0;
        for (Object[] item: lst) {
            if (Arrays.equals(item, vals)) {
                return index;
            }
            index++;
        }

        return -1;
    }


    public class RowValues {
        private int[] cols;
        private Object[] values;

        public RowValues(int[] cols, Object[] values) {
            super();
            this.cols = cols;
            this.values = values;
        }

        /**
         * @return the cols
         */
        public int[] getCols() {
            return cols;
        }

        /**
         * @return the values
         */
        public Object[] getValues() {
            return values;
        }
    }


    /**
     * @return the comparator
     */
    public Comparator<RowValues> getComparator() {
        return comparator;
    }


    /**
     * @param comparator the comparator to set
     */
    public void setComparator(Comparator<RowValues> comparator) {
        this.comparator = comparator;
    }


    /**
     * @return the rowGroupping
     */
    public boolean isRowGroupping() {
        return rowGroupping;
    }


    /**
     * @param rowGroupping the rowGroupping to set
     */
    public void setRowGroupping(boolean rowGroupping) {
        this.rowGroupping = rowGroupping;
    }

}
