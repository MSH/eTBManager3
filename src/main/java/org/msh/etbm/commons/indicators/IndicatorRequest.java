package org.msh.etbm.commons.indicators;

import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.List;
import java.util.Map;

/**
 * Request passed to {@link IndicatorReport} to generate an indicator
 *
 * Created by rmemoria on 10/9/16.
 */
public class IndicatorRequest {

    private Map<Filter, Object> filterValues;
    private List<Variable> columnVariables;
    private List<Variable> rowVariables;
    private String mainTable;

    public IndicatorRequest() {
    }

    public IndicatorRequest(String mainTable, Map<Filter, Object> filterValues, List<Variable> columnVariables, List<Variable> rowVariables) {
        this.mainTable = mainTable;
        this.filterValues = filterValues;
        this.columnVariables = columnVariables;
        this.rowVariables = rowVariables;
    }

    public String getMainTable() {
        return mainTable;
    }

    public void setMainTable(String mainTable) {
        this.mainTable = mainTable;
    }

    public Map<Filter, Object> getFilterValues() {
        return filterValues;
    }

    public void setFilterValues(Map<Filter, Object> filterValues) {
        this.filterValues = filterValues;
    }

    public List<Variable> getColumnVariables() {
        return columnVariables;
    }

    public void setColumnVariables(List<Variable> columnVariables) {
        this.columnVariables = columnVariables;
    }

    public List<Variable> getRowVariables() {
        return rowVariables;
    }

    public void setRowVariables(List<Variable> rowVariables) {
        this.rowVariables = rowVariables;
    }
}
