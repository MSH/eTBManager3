package org.msh.etbm.services.cases.reports;

import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;

import java.util.List;

/**
 * Created by rmemoria on 20/10/16.
 */
public class ReportExecResult {

    private List<CaseIndicatorResponse> indicators;

    private CaseReportFormData schema;

    public List<CaseIndicatorResponse> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<CaseIndicatorResponse> indicators) {
        this.indicators = indicators;
    }

    public CaseReportFormData getSchema() {
        return schema;
    }

    public void setSchema(CaseReportFormData schema) {
        this.schema = schema;
    }
}
