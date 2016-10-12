package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.Item;
import org.msh.etbm.services.cases.indicators.*;
import org.msh.etbm.services.security.permissions.Permissions;
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
        UUID id = caseReportService.save(data);
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/save/{id}", method = RequestMethod.POST)
    public StandardResult updateReport(@PathVariable UUID id, @RequestBody @Valid @NotNull CaseReportFormData data) {
        caseReportService.update(id, data);
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public StandardResult queryReports() {
        List<Item<String>> lst = caseReportService.getReportList();
        return new StandardResult(lst, null, true);
    }
}
