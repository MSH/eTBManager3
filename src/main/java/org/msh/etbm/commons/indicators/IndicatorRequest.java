package org.msh.etbm.commons.indicators;

import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.List;
import java.util.Map;

/**
 * Request passed to {@link IndicatorGenerator} to generate an indicator
 *
 * Created by rmemoria on 10/9/16.
 */
public class IndicatorRequest {

    /**
     * Possible options for group sub total
     */
    public enum GroupTotal {
        NONE, BEFORE_GROUP, AFTER_GROUP;
    }

    private Map<Filter, Object> filterValues;
    private List<Variable> columnVariables;
    private List<Variable> rowVariables;
    private String mainTable;
    private boolean rowTotal = true;
    private boolean columnTotal = true;
    private GroupTotal rowGroupTotal = GroupTotal.NONE;
    private GroupTotal columnGroupTotal = GroupTotal.NONE;

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

    public boolean isRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(boolean rowTotal) {
        this.rowTotal = rowTotal;
    }

    public boolean isColumnTotal() {
        return columnTotal;
    }

    public void setColumnTotal(boolean columnTotal) {
        this.columnTotal = columnTotal;
    }

    public GroupTotal getRowGroupTotal() {
        return rowGroupTotal;
    }

    public void setRowGroupTotal(GroupTotal rowGroupTotal) {
        this.rowGroupTotal = rowGroupTotal;
    }

    public GroupTotal getColumnGroupTotal() {
        return columnGroupTotal;
    }

    public void setColumnGroupTotal(GroupTotal columnGroupTotal) {
        this.columnGroupTotal = columnGroupTotal;
    }
}
