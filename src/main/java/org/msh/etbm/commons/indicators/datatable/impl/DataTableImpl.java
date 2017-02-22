package org.msh.etbm.commons.indicators.datatable.impl;

import org.msh.etbm.commons.indicators.datatable.Column;
import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;

import java.util.*;

/**
 * Represents a structured data representation in a table format, similar to the
 * records stored in a database table or the result set of a database query, but
 * includes other features like:
 * <p/>
 * 1. The possibility to include new rows and columns, even with data already initialized;
 * <br/>
 * 2. Support key (objects) to be assigned to columns and rows
 * <br/>
 * 3. Support cell styles (column span, for example)
 *
 * @author Ricardo Memoria
 *
 */
public class DataTableImpl implements DataTable {

    private List<RowImpl> rows = new ArrayList<RowImpl>();
    private List<Column> columns = new ArrayList<Column>();


    public DataTableImpl() {
        super();
    }


    public DataTableImpl(int colcount, int rowcount) {
        super();
        resize(colcount, rowcount);
    }

    /**
     * Create a new row
     * @return instance of the {@link RowImpl} class
     */
    protected RowImpl createRow() {
        return new RowImpl(this);
    }


    /**
     * Create a new column class
     * @return instance of the {@link ColumnImpl} class
     */
    protected ColumnImpl createColumn() {
        return new ColumnImpl(this);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getValue(int, int)
     */
    @Override
    public Object getValue(int colindex, int rowindex) {
        Row row = rows.get(rowindex);
        return row.getValue(colindex);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#setValue(int, int, java.lang.Object)
     */
    @Override
    public void setValue(int colindex, int rowindex, Object value) {
        getRows().get(rowindex).setValue(colindex, value);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#insertColumn(int)
     */
    @Override
    public Column insertColumn(int colindex) {
        if ((colindex < 0) || (colindex >= getColumnCount())) {
            throw new IllegalArgumentException("Illegal column index " + colindex);
        }

        // add new column
        ColumnImpl col = createColumn();
        columns.add(colindex, col);

        for (RowImpl row: rows) {
            row.insertCol(colindex);
        }

        return col;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#addColumn()
     */
    @Override
    public Column addColumn() {
        // it's not necessary to resize the rows because they are dinamically resized
        ColumnImpl col = createColumn();
        columns.add(col);
        return col;
    }


    /**
     * Insert a new row in the data table at the position rowindex
     * @param rowindex
     */
    public Row insertRow(int rowindex) {
        if ((rowindex < 0) || (rowindex > getRowCount())) {
            throw new IllegalArgumentException("Illegal column index " + rowindex);
        }

        // add a new row
        RowImpl row = createRow();

        // check if it's the last row to be included
        if (rowindex == getRowCount()) {
            rows.add(row);
        } else {
            rows.add(rowindex, row);
        }
        return row;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#addRow()
     */
    @Override
    public Row addRow() {
        RowImpl row = createRow();
        rows.add(row);
        return row;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#resize(int, int)
     */
    @Override
    public void resize(int numcols, int numrows) {
        if ((numcols == columns.size()) && (numrows == rows.size())) {
            return;
        }

        // update number of rows
        if (numrows > rows.size()) {
            while (rows.size() < numrows) {
                rows.add(createRow());
            }
        } else {
            while (rows.size() > numrows) {
                rows.remove(rows.size() - 1);
            }
        }

        if (columns.size() != numcols) {
            for (Row row: rows) {
                ((RowImpl)row).resize(numcols);
            }

            // update number of columns
            if (numcols > columns.size()) {
                while (columns.size() < numcols) {
                    columns.add(createColumn());
                }
            } else {
                while (columns.size() > numcols) {
                    columns.remove(columns.size() - 1);
                }
            }
        }
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#removeRow(int)
     */
    @Override
    public void removeRow(int rowindex) {
        rows.remove(rowindex);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#removeColumn(int)
     */
    @Override
    public void removeColumn(int colindex) {
        columns.remove(colindex);

        for (Row row: rows) {
            ((RowImpl) row).removeCol(colindex);
        }
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#swapRow(int, int)
     */
    @Override
    public void swapRow(int rowindex1, int rowindex2) {
        RowImpl row1 = rows.get(rowindex1);
        RowImpl row2 = rows.get(rowindex2);

        rows.set(rowindex2, row1);
        rows.set(rowindex1, row2);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#findColumn(int[], java.lang.Object[], int)
     */
    @Override
    public Column findColumn(int[] rowindexes, Object[] values, int colini) {
        for (int c = colini; c < columns.size(); c++) {
            boolean equals = true;
            int i = 0;

            for (int r: rowindexes) {
                Object val = getValue(c, r);
                if (!equalObjects(val, values[i++])) {
                    equals = false;
                    break;
                }
            }

            if (equals) {
                return columns.get(c);
            }
        }

        return null;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#findRow(int[], java.lang.Object[], int)
     */
    @Override
    public Row findRow(int[] colindexes, Object[] values, int rowini) {
        for (int rindex = rowini; rindex < rows.size(); rindex++) {
            int i = 0;
            boolean equals = true;
            Row row = (Row)rows.get(rindex);

            for (int index: colindexes) {
                Object val = row.getValue(index);
                if (!equalObjects(val, values[i++])) {
                    equals = false;
                    break;
                }
            }

            if (equals) {
                return row;
            }
        }

        return null;
    }


    /**
     * Compare two objects
     * @param obj1
     * @param obj2
     * @return
     */
    protected boolean equalObjects(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }

        if ((obj1 == null) || (obj2 == null)) {
            return false;
        }

        return obj1.equals(obj2);
    }



    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columns.size();
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getRowCount()
     */
    @Override
    public int getRowCount() {
        return rows.size();
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getRows()
     */
    @Override
    public List<Row> getRows() {
        return new AbstractList<Row>() {
            @Override
            public Row get(int index) {
                return (Row)getRow(index);
            }

            @Override
            public int size() {
                return rows.size();
            }
        };
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getColumns()
     */
    @Override
    public List<Column> getColumns() {
        return new AbstractList<Column>() {
            @Override
            public Column get(int index) {
                return (Column)getColumn(index);
            }

            @Override
            public int size() {
                return columns.size();
            }
        };
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getColumn(int)
     */
    @Override
    public Column getColumn(int index) {
        return columns.get(index);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#getRow(int)
     */
    @Override
    public Row getRow(int index) {
        return rows.get(index);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.DataTable#sortRows(java.util.Comparator)
     */
    @Override
    public void sortRows(Comparator<Row> comparator) {
        Collections.sort(rows, comparator);
    }
}
