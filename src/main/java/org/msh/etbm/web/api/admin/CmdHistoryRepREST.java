package org.msh.etbm.web.api.admin;

import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepService;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by msantos on 11/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_USERSONLINE})
public class CmdHistoryRepREST {

    @Autowired
    CmdHistoryRepService service;

    @RequestMapping(value = "/cmdhistory", method = RequestMethod.POST)
    public List<OnlineUsersRepData> query() {
        return service.getResult();
    }

}
