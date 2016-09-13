package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.datatable.Column;
import org.msh.etbm.commons.indicators.datatable.DataTableUtils;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.datatable.impl.RowImpl;

import java.util.*;

/**
 * Base implementation of the {@link DataTableIndicator} interface
 * @author Ricardo Memoria
 *
 */
public class DataTableIndicatorImpl extends DataTableImpl implements DataTableIndicator {

    private List<HeaderRow> headerRows = new ArrayList<HeaderRow>();

    // indicate if it's an internal operation
    private boolean internalOperation;

    public DataTableIndicatorImpl() {
        super();
    }

    public DataTableIndicatorImpl(int colcount, int rowcount) {
        super();
        resize(colcount, rowcount);
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#addRow(org.msh.etbm.commons.indicators.indicator.IndicatorRow)
     */
    @Override
    public IndicatorRow addIndicatorRow(IndicatorRow parent) {
        // get the new index of the row
        if (parent == null) {
            return (IndicatorRow)addRow();
        }

        int index = parent.getIndex() + 1;
        int level = parent.getLevel();

        // get the next sibling row
        while ((index < getRows().size()) && (getIndicatorRows().get(index).getLevel() > level)) {
            index++;
        }

        IndicatorRow row = (IndicatorRow)insertRow(index);
        ((IndicatorRowImpl)parent).addChildRow(row);

        return row;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#getIndicatorColumns()
     */
    @Override
    public List<IndicatorColumn> getIndicatorColumns() {
        List<IndicatorColumn> lst = new ArrayList<IndicatorColumn>();
        if (headerRows.size() == 0) {
            return lst;
        }

        for (IndicatorColumn col: headerRows.get(0).getColumns()) {
            col.addEndPointColumns(lst);
        }

        return lst;
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#addColumn(org.msh.etbm.commons.indicators.indicator.IndicatorColumn)
     */
    @Override
    public IndicatorColumn addIndicatorColumn(IndicatorColumn parent) {
        return addIndicatorColumn(parent, null);
    }


    /**
     * Add a column as the child of a given column. If no column is
     * specified as the parent, the column is included as a top level column. In this
     * case, the position where the top level column will be included can be
     * specified.
     * @param parent the parent column of the new column to be added, or null if
     * this column has no parent
     * @return instance of the {@link IndicatorColumn} class
     */
    protected IndicatorColumn addIndicatorColumn(IndicatorColumn parent, Integer level0position) {
        internalOperation = true;
        try {
            // get the level of the new column
            int level = parent == null ? 0 : parent.getLevel() + 1;

            // check if it's a new header row
            if (level == headerRows.size()) {
                headerRows.add(new HeaderRow());
            }

            // get the header row
            HeaderRow headerRow = headerRows.get(level);

            // create the column
            IndicatorColumn indColumn = new IndicatorColumnImpl(parent);

            // if this is the only child of the parent, so no new column
            // must be included, because it was already by the parent
            if ((parent != null) && (parent.getColumns().size() == 1)) {
                indColumn.setIndex(parent.getIndex());
                addColumnInHeaderRow(headerRow, indColumn);
                return indColumn;
            }

            // add the column to the header row
            // is the first level?
            if (level == 0) {
                // no specific position was specified ?
                if (level0position == null) {
                    // add the column and finish
                    headerRow.getColumns().add(indColumn);
                    Column col = addColumn();
                    indColumn.setIndex(col.getIndex());
                    return indColumn;
                } else {
                    headerRow.getColumns().add(level0position, indColumn);
                    insertColumn(level0position);
                    int index = 0;
                    for (IndicatorColumn aux: getIndicatorColumns()) {
                        aux.setIndex(index++);
                    }
                    return indColumn;
                }
            }

            List<IndicatorColumn> lst = getIndicatorColumns();

            int index = lst.indexOf(indColumn);
            for (int i = index; i < lst.size(); i++) {
                lst.get(i).setIndex(i);
            }

            // new indicator column is in the last column ?
            if (index == lst.size() - 1) {
                // add new column at the right side of the table
                addColumn();
            } else {
                insertColumn(index); // insert column in the specific position
            }

            addColumnInHeaderRow(headerRow, indColumn);

            return indColumn;
        } finally {
            internalOperation = false;
        }
    }

    protected void addColumnInHeaderRow(HeaderRow headerRow, IndicatorColumn col) {
        // calculate the position of the column in the row header
        List<IndicatorColumn> cols = headerRow.getColumns();
        int index = cols.size();
        while (index > 0) {
            IndicatorColumn par = cols.get(index - 1).getParent();
            if (par == col.getParent()) {
                break;
            }
            index--;
        }

        // insert column in the row header
        if (index == 0) {
            cols.add(col);
        } else {
            cols.add(index, col);
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#addIndicatorValue(java.lang.Object[], java.lang.Object[], double)
     */
    @Override
    public void addIndicatorValue(Object[] colkeys, Object[] rowkeys, double value) {
        if (colkeys.length == 0) {
            throw new IllegalArgumentException("No column keys specified");
        }

        if (rowkeys.length == 0) {
            throw new IllegalArgumentException("No row keys specified");
        }

        if (colkeys.length < headerRows.size()) {
            throw new IllegalArgumentException("Number of elements in the column key doesn't match with the system. Found " +
                    colkeys.length + " expected" + headerRows.size());
        }

        // search for the column of the table
        IndicatorColumn cparent = null;
        for (Object key: colkeys) {
            IndicatorColumn col = findColumnByKey(key, cparent);
            if (col == null) {
                col = addIndicatorColumn(cparent);
                col.setKey(key);
            }
            cparent = col;
        }
        IndicatorColumn col = cparent;
        if (!col.isEndPointColumn()) {
            throw new IllegalArgumentException("Column selected is not an end point column");
        }

        // search for the row of the table
        IndicatorRow rparent = null;
        for (Object key: rowkeys) {
            IndicatorRow row = findRowByKey(key, rparent);
            if (row == null) {
                row = addIndicatorRow(rparent);
                row.setKey(key);
            }
            rparent = row;
        }
        IndicatorRow row = rparent;;
        if (!row.isEndPointRow()) {
            throw new IllegalArgumentException("Row selected is not an end point column");
        }

        // set the value in the table
        while (row != null) {
            int rowindex = row.getIndex();
            int colindex = col.getIndex();
            Double prevval = (Double)getValue(colindex, rowindex);
            setValue(colindex, rowindex, value + (prevval != null ? prevval : 0));
            row = row.getParent();
        }
    }

    /**
     * Search for a column by its key. If the parent column is not specified, so the
     * list of the top columns will be used
     * @param key is the key of the column that is been searched
     * @param parent is the parent column where the child will be searched, or null if it's the top columns
     * @return instance of the {@link IndicatorColumn} class, or null if no column was found
     */
    protected IndicatorColumn findColumnByKey(Object key, IndicatorColumn parent) {
        List<IndicatorColumn> cols;
        if (parent == null) {
            if (headerRows.size() == 0) {
                return null;
            }
            cols = headerRows.get(0).getColumns();
        } else {
            cols = parent.getColumns();
            if (cols == null) {
                return null;
            }
        }

        for (IndicatorColumn col: cols) {
            if (DataTableUtils.equalValue(col.getKey(), key)) {
                return col;
            }
        }

        return null;
    }

    /**
     * Search for a row by its key. If the parent row is not specified, so the
     * list of the top row will be used
     * @param key is the key of the row that is been searched
     * @param parent is the parent row where the child will be searched, or null if it's the top rows
     * @return instance of the {@link IndicatorColumn} class, or null if no column was found
     */
    protected IndicatorRow findRowByKey(Object key, IndicatorRow parent) {
        // there is no parent ?
        if (parent == null) {
            // search in all rows of level = 0
            for (IndicatorRow row: getIndicatorRows()) {
                if ((row.getLevel() == 0) && (DataTableUtils.equalValue(row.getKey(), key))) {
                    return row;
                }
            }
        } else {
            // search in the row children of the parent
            if (parent.getRows() == null) {
                return null;
            }

            for (IndicatorRow row: parent.getRows()) {
                if (DataTableUtils.equalValue(row.getKey(), key)) {
                    return row;
                }
            }
        }

        return null;
    }

    @Override
    public Double getIndicatorValue(int colindex, int rowindex) {
        return (Double)getValue(colindex, rowindex);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#getHeaderRowsCount()
     */
    @Override
    public int getHeaderRowsCount() {
        return headerRows.size();
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#getHeaderRow(int)
     */
    @Override
    public HeaderRow getHeaderRow(int row) {
        return headerRows.get(row);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#getIndicatorRow(int)
     */
    @Override
    public IndicatorRow getIndicatorRow(int row) {
        return (IndicatorRow)getRow(row);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#getIndicatorRows()
     */
    @Override
    public List<IndicatorRow> getIndicatorRows() {
        // return an unmodifiable list of rows
        return new AbstractList<IndicatorRow>() {
            @Override
            public IndicatorRow get(int index) {
                return getIndicatorRow(index);
            }

            @Override
            public int size() {
                return getRows().size();
            }
        };
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#createRow()
     */
    @Override
    protected RowImpl createRow() {
        return new IndicatorRowImpl(this);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#addIndicatorRow()
     */
    @Override
    public IndicatorRow addIndicatorRow() {
        return addIndicatorRow(null);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#addIndicatorColumn()
     */
    @Override
    public IndicatorColumn addIndicatorColumn() {
        return addIndicatorColumn(null);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#insertColumn(int)
     */
    @Override
    public Column insertColumn(int colindex) {
        if (internalOperation) {
            return super.insertColumn(colindex);
        } else {
            IndicatorColumn indCol = addIndicatorColumn(null, colindex);
            return getColumn( indCol.getIndex() );
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#addColumn()
     */
    @Override
    public Column addColumn() {
        if (internalOperation) {
            return super.addColumn();
        } else {
            IndicatorColumn indCol = addIndicatorColumn(null);
            return getColumn( indCol.getIndex() );
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#removeIndicatorColumn(int)
     */
    @Override
    public void removeIndicatorColumn(int index) {
        internalOperation = true;
        try {
            removeColumn(index);

            List<IndicatorColumn> lst = getIndicatorColumns();
            IndicatorColumn col = lst.get(index);
            // remove column and its parent, unless they are not end point
            while (col.getParent() != null) {
                int level = col.getLevel();
                headerRows.get(level).getColumns().remove(col);
                ((IndicatorColumnImpl)col.getParent()).removeColumn(col);
                col = col.getParent();
                if (!col.isEndPointColumn()) {
                    break;
                }
            }

            // update indexes
            lst = getIndicatorColumns();
            int i = 0;
            for (IndicatorColumn aux: lst) {
                aux.setIndex(i++);
            }
        } finally {
            internalOperation = false;
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#removeIndicatorRow(int)
     */
    @Override
    public void removeIndicatorRow(int index) {
        removeRow(index);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.indicator.DataTableIndicator#getIndicatorColumn(int)
     */
    @Override
    public IndicatorColumn getIndicatorColumn(int colindex) {
        return getIndicatorColumns().get(colindex);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#removeRow(int)
     */
    @Override
    public void removeRow(int rowindex) {
        IndicatorRow row = getIndicatorRow(rowindex);
        super.removeRow(rowindex);

        if (row.getRows() != null) {
            for (IndicatorRow childrow: row.getRows()) {
                removeRow(childrow.getIndex());
            }
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#removeColumn(int)
     */
    @Override
    public void removeColumn(int colindex) {
        if (internalOperation) {
            super.removeColumn(colindex);
        } else {
            removeIndicatorColumn(colindex);
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#swapRow(int, int)
     */
    @Override
    public void swapRow(int rowindex1, int rowindex2) {
        raiseNotSupportedException();
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#resize(int, int)
     */
    @Override
    public void resize(int numcols, int numrows) {
        while (numcols > getColumnCount()) {
            addColumn();
        }

        while (numrows > getRowCount()) {
            addRow();
        }

        while (numcols < getColumnCount()) {
            removeColumn(getColumnCount() - 1);
        }

        while (numrows < getRowCount()) {
            removeRow(getRowCount() - 1);
        }
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl#sortRows(java.util.Comparator)
     */
    @Override
    public void sortRows(Comparator<Row> comparator) {
        raiseNotSupportedException();
    }


    protected void raiseNotSupportedException() {
        throw new UnsupportedOperationException("Not supported for " + getClass().toString());
    }

}
