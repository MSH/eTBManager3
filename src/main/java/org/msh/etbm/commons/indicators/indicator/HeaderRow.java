package org.msh.etbm.commons.indicators.indicator;

import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a row of the header in the indicator table. It stores the
 * instance of the {@link Variable} assigned to the row, if it is a
 * group value and the list of columns
 *
 * @author Ricardo Memoria
 *
 */
public class HeaderRow {

    private List<IndicatorColumn> columns = new ArrayList<IndicatorColumn>();
    private Variable variable;

    /**
     * Return the index position of the given column in the list of columns
     * @param col instance of the {@link IndicatorColumn}
     * @return
     */
    protected int getColumnIndex(IndicatorColumn col) {
        return columns.indexOf(col);
    }

    /**
     * @return the columns
     */
    public List<IndicatorColumn> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<IndicatorColumn> columns) {
        this.columns = columns;
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
}
