package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.onlinereport.OnlineReportService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rmemoria on 6/1/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_USERSONLINE})
public class OnlineReportREST {

    @Autowired
    OnlineReportService service;

    @RequestMapping(value = "/onlinereport", method = RequestMethod.POST)
    public QueryResult query() {
        return service.getResult();
    }
}
