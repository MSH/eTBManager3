package org.msh.etbm.services.dashboard;

import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.db.entities.Report;
import org.msh.etbm.services.cases.reports.CaseReportFormData;
import org.msh.etbm.services.cases.reports.CaseReportIndicatorData;
import org.msh.etbm.services.cases.reports.CaseReportService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service to generate a dashboard to be displayed in the workspace, admin unit or unit page
 *
 * Created by rmemoria on 22/10/16.
 */
@Service
public class DashboardService {


    @Autowired
    CaseReportService caseReportService;

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Generate the dashboard data by the given scope
     * @param req
     * @return
     */
    public DashboardResponse generate(DashboardRequest req) {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();

        List<Report> lst = entityManager.createQuery("from Report where workspace.id = :id and dashboard = true")
                .setParameter("id", wsId)
                .getResultList();

        List<CaseReportIndicatorData> indicators = new ArrayList<>();

        for (Report rep: lst) {
            CaseReportFormData schema = JsonUtils.parseString(rep.getData(), CaseReportFormData.class);
            List<CaseReportIndicatorData> inds = caseReportService.generateIndicators(schema, req.getScope(), req.getScopeId());
            indicators.addAll(inds);
        }

        DashboardResponse resp = new DashboardResponse();
        resp.setCaseIndicators(indicators);

        return resp;
    }
}
