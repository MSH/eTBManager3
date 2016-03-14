package org.msh.etbm.web.api.admin;

import org.msh.etbm.services.admin.onlinereport.OnlineReportData;
import org.msh.etbm.services.admin.onlinereport.OnlineReportService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by msantos on 11/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_USERSONLINE})
public class OnlineReportREST {

    @Autowired
    OnlineReportService service;

    @RequestMapping(value = "/onlinereport", method = RequestMethod.POST)
    public List<OnlineReportData> query() {
        return service.getResult();
    }

}
