package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.Item;
import org.msh.etbm.services.cases.indicators.CaseIndicatorInitResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorRequest;
import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;
import org.msh.etbm.services.cases.indicators.CaseIndicatorsService;
import org.msh.etbm.services.cases.reports.CaseReportFormData;
import org.msh.etbm.services.cases.reports.CaseReportService;
import org.msh.etbm.services.cases.reports.ReportExecRequest;
import org.msh.etbm.services.cases.reports.ReportExecResult;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * REST API for case indicator generation
 *
 * Created by rmemoria on 5/10/16.
 */
@RestController
@RequestMapping("/api/cases/report")
@Authenticated(permissions = {Permissions.CASES})
public class CaseReportsREST {

    @Autowired
    CaseIndicatorsService caseIndicatorsService;

    @Autowired
    CaseReportService caseReportService;

    @Autowired
    UserRequestService userRequestService;


    @RequestMapping(value = "/ind/exec", method = RequestMethod.POST)
    public CaseIndicatorResponse generateIndicator(@RequestBody @Valid @NotNull CaseIndicatorRequest req) {
        return caseIndicatorsService.execute(req);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public CaseIndicatorInitResponse getInitData() {
        return caseIndicatorsService.getInitData();
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public StandardResult saveReport(@Valid @RequestBody @NotNull CaseReportFormData data) {
        UUID userId = userRequestService.getUserSession().getUserId();
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();
        UUID id = caseReportService.save(data, userId, wsId);
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/save/{id}", method = RequestMethod.POST)
    public StandardResult updateReport(@PathVariable UUID id, @RequestBody @Valid @NotNull CaseReportFormData data) {
        caseReportService.update(id, data);
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public StandardResult queryReports() {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();
        List<Item<String>> lst = caseReportService.getReportList(wsId);
        return new StandardResult(lst, null, true);
    }

    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    public StandardResult get(@RequestBody @Valid @NotNull ReportExecRequest req) {
        req.setScopeId(userRequestService.getUserSession().getWorkspaceId());
        ReportExecResult res = caseReportService.execute(req);
        return new StandardResult(res, null, true);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public StandardResult delete(@PathVariable UUID id) {
        caseReportService.delete(id);
        return StandardResult.createSuccessResult();
    }
}
