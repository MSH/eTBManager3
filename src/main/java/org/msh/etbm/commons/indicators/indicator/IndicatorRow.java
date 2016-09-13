package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.datatable.impl.RowImpl;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public interface IndicatorRow extends Row {

    /**
     * Return the level of the row. The level determines the position
     * of the rows in its rows
     * @return level of the column starting at 0
     */
    int getLevel();

    /**
     * Return true if the row is the last row in the list of row levels
     * @return true if there is no other child row for this row
     */
    boolean isEndPointRow();

    /**
     * Return the title to be used by the row
     * @return the title
     */
    String getTitle();

    /**
     * Set the row title
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
     * @return the variable
     */
    Variable getVariable();

    /**
     * @param variable the variable to set
     */
    void setVariable(Variable variable);

    /**
     * @return the parent
     */
    IndicatorRow getParent();

    /**
     * @return the rows
     */
    List<IndicatorRow> getRows();

}
