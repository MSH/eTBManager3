package org.msh.etbm.services.cases.indicators;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Indicator data to be saved inside a report. Used as a reference list by {@link CaseReportFormData}
 *
 * Created by rmemoria on 11/10/16.
 */
public class CaseIndicatorFormData {

    @NotNull
    private String title;

    private Map<String, Object> filters;

    private String chart;
    private Integer display;
    private List<String> rowVariables;
    private List<String> columnVariables;
    private Integer size;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

    public String getChart() {
        return chart;
    }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public Integer getDisplay() {
        return display;
    }

    public void setDisplay(Integer display) {
        this.display = display;
    }

    public List<String> getRowVariables() {
        return rowVariables;
    }

    public void setRowVariables(List<String> rowVariables) {
        this.rowVariables = rowVariables;
    }

    public List<String> getColumnVariables() {
        return columnVariables;
    }

    public void setColumnVariables(List<String> columnVariables) {
        this.columnVariables = columnVariables;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
