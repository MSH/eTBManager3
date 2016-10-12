package org.msh.etbm.services.cases.indicators;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * Data about a report to be saved or updated
 * Created by rmemoria on 11/10/16.
 */
public class CaseReportFormData {

    @NotNull
    private String title;

    private Map<String, Object> filters;

    @Valid
    @NotNull
    private List<CaseIndicatorFormData> indicators;

    private boolean published;

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

    public List<CaseIndicatorFormData> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<CaseIndicatorFormData> indicators) {
        this.indicators = indicators;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isDashboard() {
        return dashboard;
    }

    public void setDashboard(boolean dashboard) {
        this.dashboard = dashboard;
    }
}
