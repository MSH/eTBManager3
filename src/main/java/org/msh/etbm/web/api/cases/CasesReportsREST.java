package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.Item;
import org.msh.etbm.services.cases.reports.ReportRequest;
import org.msh.etbm.services.cases.reports.ReportsService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * API to generate reports of the case management module
 *
 * Created by rmemoria on 23/9/16.
 */
@RestController
@RequestMapping("/api/cases/reports")
@Authenticated(permissions = {Permissions.CASES})
public class CasesReportsREST {

    @Autowired
    ReportsService reportsService;

    /**
     * Return the list of available report for the give scope
     * @param req
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public List<Item> getResult(@RequestBody @Valid @NotNull ReportRequest req) {
        return reportsService.getReports(req);
    }
}
