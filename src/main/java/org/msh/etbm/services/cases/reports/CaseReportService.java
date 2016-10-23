package org.msh.etbm.services.cases.reports;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.db.entities.Report;
import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.cases.indicators.CaseIndicatorFormData;
import org.msh.etbm.services.cases.indicators.CaseIndicatorRequest;
import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorsService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.util.UUID;
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
    UserRequestService userRequestService;

    @Autowired
    CaseIndicatorsService caseIndicatorsService;


    @Transactional
    public UUID save(CaseReportFormData repdata) {
        String data = JsonParser.objectToJSONString(repdata, false);

        Report rep = new Report();
        updateData(rep, repdata);

        UUID userid = userRequestService.getUserSession().getUserId();
        User user = entityManager.find(User.class, userid);

        rep.setOwner(user);
        rep.setRegistrationDate(new Date());

        UUID wsId = userRequestService.getUserSession().getWorkspaceId();
        Workspace ws = entityManager.find(Workspace.class, wsId);
        rep.setWorkspace(ws);

        entityManager.persist(rep);

        return rep.getId();
    }

    @Transactional
    public CaseReportFormData load(UUID id) {
        Report rep = entityManager.find(Report.class, id);

        CaseReportFormData data = JsonParser.parseString(rep.getData(), CaseReportFormData.class);

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
        String data = JsonParser.objectToJSONString(repdata, false);

        rep.setDashboard(repdata.isDashboard());
        rep.setPublished(repdata.isPublished());
        rep.setData(data);

        rep.setTitle(repdata.getTitle());
    }

    @Transactional
    public List<Item<String>> getReportList() {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();

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


    public CaseReportFormData execute(ReportExecRequest req) {
        Report rep = entityManager.find(Report.class, req.getReportId());

        if (rep == null) {
            throw new EntityNotFoundException("Report not found");
        }

        CaseReportFormData schema = JsonParser.parseString(rep.getData(), CaseReportFormData.class);

        // generate the indicators
        List<CaseIndicatorFormData> lst = schema.getIndicators()
                .stream()
                .map(ind -> {
                    // generate indicator
                    CaseIndicatorRequest indreq = new CaseIndicatorRequest();
                    indreq.setFilters(ind.getFilters());
                    indreq.setColumnVariables(ind.getColumnVariables());
                    indreq.setRowVariables(ind.getRowVariables());
                    indreq.setScope(req.getScope());
                    indreq.setScopeId(req.getScopeId());
                    CaseIndicatorResponse resp = caseIndicatorsService.execute(indreq);

                    // pack response
                    CaseIndicatorData inddata = new CaseIndicatorData();
                    ObjectUtils.copyObject(ind, inddata);
                    inddata.setData(resp.getIndicator());

                    return inddata;
                })
                .collect(Collectors.toList());

        schema.setIndicators(lst);

        return schema;
    }
}