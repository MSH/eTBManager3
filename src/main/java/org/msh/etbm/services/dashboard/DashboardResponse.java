package org.msh.etbm.services.dashboard;

import org.msh.etbm.services.cases.reports.CaseReportIndicatorData;

import java.util.List;

/**
 * Created by rmemoria on 22/10/16.
 */
public class DashboardResponse {

    /**
     * Case indicators data and schema
     */
    private List<CaseReportIndicatorData> caseIndicators;

    public List<CaseReportIndicatorData> getCaseIndicators() {
        return caseIndicators;
    }

    public void setCaseIndicators(List<CaseReportIndicatorData> caseIndicators) {
        this.caseIndicators = caseIndicators;
    }
}
