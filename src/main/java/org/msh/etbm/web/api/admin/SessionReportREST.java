package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.onlinereport.OnlineReportService;
import org.msh.etbm.services.admin.sessionreport.SessionReportData;
import org.msh.etbm.services.admin.sessionreport.SessionReportQueryParams;
import org.msh.etbm.services.admin.sessionreport.SessionReportService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by msantos on 10/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_USERSESSIONS})
public class SessionReportREST {

    @Autowired
    SessionReportService service;

    @RequestMapping(value = "/dailysessionreport", method = RequestMethod.POST)
    public QueryResult queryFilterByDay(@Valid @RequestBody SessionReportQueryParams query) {
        if (query != null) {
            System.out.println("hey hey hey");
            System.out.println("iniDate: " + query.getIniDate());
            System.out.println("endDate: " + query.getEndDate());
            System.out.println("userId: " + query.getUserId());
        }
        return service.getResultByDay(query.getIniDate());
    }
}
