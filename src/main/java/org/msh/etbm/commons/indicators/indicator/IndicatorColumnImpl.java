package org.msh.etbm.commons.indicators.indicator;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 10/9/16.
 */
public class IndicatorColumnImpl implements IndicatorColumn {

    private String title;
    private Object key;
    private IndicatorColumn parent;
    private List<IndicatorColumn> columns;
    private int index;

    public IndicatorColumnImpl(IndicatorColumn parent) {
        super();
        this.parent = parent;
        if (parent != null) {
            ((IndicatorColumnImpl)parent).addColumn(this);
        }
    }

    /**
     * Add a child column
     * @param col child column to be included
     */
    protected void addColumn(IndicatorColumn col) {
        if (columns == null) {
            columns = new ArrayList<IndicatorColumn>();
        }
        columns.add(col);
    }

    /**
     * Remove a child column from the column
     * @param col is the child column to be removed
     */
    protected void removeColumn(IndicatorColumn col) {
        if ((columns == null) || (columns.indexOf(col) == -1)) {
            throw new IllegalArgumentException("Column not found");
        }
        columns.remove(col);
    }

    /**
     * Calculate the number of end point columns, including the sub columns
     * @return number of end points under this column
     */
    @Override
    public int getEndPointCount() {
        if (columns == null) {
            return 1;
        }

        int endpoints = 0;
        for (IndicatorColumn col: columns) {
            endpoints += col.getEndPointCount();
        }

        return endpoints;
    }

    /**
     * Return true if the column is the last column in the list of column childs
     * @return true if there is no other child column for this column
     */
    @Override
    public boolean isEndPointColumn() {
        return columns == null;
    }

    /**
     * Return the level of the column. The level determines the position
     * of the columns in its column rows
     * @return level of the column starting at 0
     */
    @Override
    public int getLevel() {
        int lev = 0;
        IndicatorColumn p = parent;
        while (p != null) {
            p = p.getParent();
            lev++;
        }
        return lev;
    }


    /**
     * @return the index
     */
    @Override
    public int getIndex() {
        return index;
    }

    /**
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the key
     */
    @Override
    public Object getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    @Override
    public void setKey(Object key) {
        this.key = key;
    }

    /**
     * @return the parent
     */
    @Override
    public IndicatorColumn getParent() {
        return parent;
    }

    /**
     * @return the columns
     */
    @Override
    public List<IndicatorColumn> getColumns() {
        return columns;
    }

    /**
     * If this column is an end point column, add it to the list, otherwise
     * browse all children (including children of children) and add the
     * end point columns
     * @param lst list that will be populated with the end point columns
     */
    @Override
    public void addEndPointColumns(List<IndicatorColumn> lst) {
        if (columns == null) {
            lst.add(this);
        } else {
            for (IndicatorColumn col: columns) {
                col.addEndPointColumns(lst);
            }
        }
    }

    /**
     * @param index the index to set
     */
    @Override
    public void setIndex(int index) {
        this.index = index;
    }
}
