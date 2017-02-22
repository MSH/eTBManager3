package org.msh.etbm.services.cases.reports;

import java.util.List;
import java.util.Map;

/**
 * Result of a report execution service, called by {@link CaseReportService#execute(ReportExecRequest)}
 *
 * Created by rmemoria on 20/10/16.
 */
public class ReportExecResult {

    private String title;
    private Map<String, Object> filters;
    private List<CaseReportIndicatorData> indicators;
    private boolean dashboard;

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

    public List<CaseReportIndicatorData> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<CaseReportIndicatorData> indicators) {
        this.indicators = indicators;
    }

    public boolean isDashboard() {
        return dashboard;
    }

    public void setDashboard(boolean dashboard) {
        this.dashboard = dashboard;
    }
}
