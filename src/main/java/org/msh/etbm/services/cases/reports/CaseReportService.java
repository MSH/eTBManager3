package org.msh.etbm.services.cases.reports;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.JsonUtils;
import org.msh.etbm.db.entities.Report;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.RequestScope;
import org.msh.etbm.services.cases.indicators.CaseIndicatorRequest;
import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Services to handle CRUD operation in reports
 *
 * Created by rmemoria on 11/10/16.
 */
@Service
public class CaseReportService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CaseIndicatorsService caseIndicatorsService;


    @Transactional
    public UUID save(CaseReportFormData repdata, UUID workspaceId) {
        String data = JsonUtils.objectToJSONString(repdata, false);

        Report rep = new Report();
        updateData(rep, repdata);

        rep.setRegistrationDate(new Date());

        Workspace ws = entityManager.find(Workspace.class, workspaceId);
        rep.setWorkspace(ws);

        entityManager.persist(rep);

        return rep.getId();
    }

    @Transactional
    public CaseReportFormData load(UUID id) {
        Report rep = entityManager.find(Report.class, id);

        CaseReportFormData data = JsonUtils.parseString(rep.getData(), CaseReportFormData.class);

        return data;
    }

    @Transactional
    public void update(UUID id, CaseReportFormData repdata) {
        Report rep = entityManager.find(Report.class, id);
        updateData(rep, repdata);

        entityManager.persist(rep);
        entityManager.flush();
    }

    protected void updateData(Report rep, CaseReportFormData repdata) {
        String data = JsonUtils.objectToJSONString(repdata, false);

        rep.setDashboard(repdata.isDashboard());
        rep.setPublished(repdata.isPublished());
        rep.setData(data);

        rep.setTitle(repdata.getTitle());
    }

    @Transactional
    public List<Item<String>> getReportList(UUID wsId) {

        List<Object[]> lst = entityManager.createQuery("select id, title " +
                  "from Report where workspace.id = :id " +
                "order by title")
                .setParameter("id", wsId)
                .getResultList();

        List<Item<String>> res = lst
                .stream()
                .map(vals -> new Item<String>(((UUID)vals[0]).toString(), (String)vals[1]))
                .collect(Collectors.toList());

        return res;
    }


    public ReportExecResult execute(ReportExecRequest req) {
        if (req.getScopeId() == null) {
            throw new IllegalArgumentException("Scope ID must be specified");
        }

        Report rep = entityManager.find(Report.class, req.getReportId());

        if (rep == null) {
            throw new EntityNotFoundException("Report not found");
        }

        CaseReportFormData schema = JsonUtils.parseString(rep.getData(), CaseReportFormData.class);

        // generate the indicators
        List<CaseReportIndicatorData> lst = generateIndicators(schema, req.getScope(), req.getScopeId());

        ReportExecResult res = new ReportExecResult();
        res.setTitle(schema.getTitle());
        res.setFilters(schema.getFilters());
        res.setIndicators(lst);
        res.setDashboard(rep.isDashboard());

        return res;
    }

    /**
     * Generate the list of indicator data based on the report form data
     * @param rep
     * @param scope
     * @param scopeId
     * @return
     */
    public List<CaseReportIndicatorData> generateIndicators(CaseReportFormData rep, RequestScope scope, UUID scopeId) {
        List<CaseReportIndicatorData> lst = rep.getIndicators()
                .stream()
                .map(ind -> {
                    // mix filters from report with indicator
                    Map<String, Object> filters = rep.getFilters() != null ?
                            new HashMap<>(rep.getFilters()) :
                            new HashMap<>();
                    if (ind.getFilters() != null) {
                        filters.putAll(ind.getFilters());
                    }

                    // generate indicator
                    CaseIndicatorRequest indreq = new CaseIndicatorRequest();
                    indreq.setFilters(filters);
                    indreq.setColumnVariables(ind.getColumnVariables());
                    indreq.setRowVariables(ind.getRowVariables());
                    indreq.setScope(scope);
                    indreq.setScopeId(scopeId);
                    CaseIndicatorResponse resp = caseIndicatorsService.execute(indreq);

                    // pack response
                    CaseReportIndicatorData inddata = new CaseReportIndicatorData();
                    inddata.setData(resp.getIndicator());
                    inddata.setSchema(ind);

                    return inddata;
                })
                .collect(Collectors.toList());

        return lst;
    }

    @Transactional
    public void delete(UUID id) {
        Report rep = entityManager.find(Report.class, id);
        if (rep == null) {
            throw new EntityNotFoundException("Report not found");
        }

        entityManager.remove(rep);
        entityManager.flush();
    }
}
