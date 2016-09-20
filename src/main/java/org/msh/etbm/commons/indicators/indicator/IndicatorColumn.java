package org.msh.etbm.commons.indicators.indicator;

import java.util.List;

public interface IndicatorColumn {

    /**
     * Calculate the number of end point columns, including the sub columns
     * @return number of end points under this column
     */
    int getEndPointCount();

    /**
     * Return true if the column is the last column in the list of column childs
     * @return true if there is no other child column for this column
     */
    boolean isEndPointColumn();

    /**
     * Return the level of the column. The level determines the position
     * of the columns in its column rows
     * @return level of the column starting at 0
     */
    int getLevel();


    /**
     * @return the index
     */
    int getIndex();

    /**
     * @return the title
     */
    String getTitle();

    /**
     * @param title the title to set
     */
    void setTitle(String title);

    /**
     * @return the key
     */
    Object getKey();

    /**
     * @param key the key to set
     */
    void setKey(Object key);

    /**
     * @return the parent
     */
    IndicatorColumn getParent();

    /**
     * @return the columns
     */
    List<IndicatorColumn> getColumns();

    /**
     * If this column is an end point column, add it to the list, otherwise
     * browse all children (including children of children) and add the
     * end point columns
     * @param lst list that will be populated with the end point columns
     */
    void addEndPointColumns(List<IndicatorColumn> lst);

    /**
     * @param index the index to set
     */
    void setIndex(int index);
}
