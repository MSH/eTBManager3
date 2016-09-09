package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.datatable.impl.RowImpl;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.List;

public class IndicatorRow extends RowImpl {

    private String title;
    private Object key;
    private Variable variable;
    private IndicatorRow parent;
    private List<IndicatorRow> rows;

    public IndicatorRow(DataTableImpl dataTable) {
        super(dataTable);
    }


    /**
     * Return the level of the row. The level determines the position
     * of the rows in its rows
     * @return level of the column starting at 0
     */
    public int getLevel() {
        int lev = 0;
        IndicatorRow p = parent;
        while (p != null) {
            p = p.getParent();
            lev++;
        }
        return lev;
    }

    /**
     * Return true if the row is the last row in the list of row levels
     * @return true if there is no other child row for this row
     */
    public boolean isEndPointRow() {
        return rows == null;
    }


    /**
     * Add a child row to this row. This method is protected because row insertion
     * is controlled by the {@link DataTableImpl} class
     * @param row is the new child row of this row
     */
    protected void addChildRow(IndicatorRow row) {
        if (rows == null) {
            rows = new ArrayList<>();
        }

        rows.add(row);
        row.setParent(this);
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    /**
     * @param title the title to set
     */

    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * @return the key
     */

    public Object getKey() {
        return key;
    }
    /**
     * @param key the key to set
     */

    public void setKey(Object key) {
        this.key = key;
    }
    /**
     * @return the variable
     */

    public Variable getVariable() {
        return variable;
    }
    /**
     * @param variable the variable to set
     */

    public void setVariable(Variable variable) {
        this.variable = variable;
    }
    /**
     * @return the parent
     */

    public IndicatorRow getParent() {
        return parent;
    }
    /**
     * @return the rows
     */

    public List<IndicatorRow> getRows() {
        return rows;
    }


    /**
     * @param parent the parent to set
     */
    public void setParent(IndicatorRow parent) {
        this.parent = parent;
    }
}
