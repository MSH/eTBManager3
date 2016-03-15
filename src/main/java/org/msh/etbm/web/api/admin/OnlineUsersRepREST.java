package org.msh.etbm.web.api.admin;

import org.msh.etbm.services.admin.onlinereport.OnlineUsersRepData;
import org.msh.etbm.services.admin.onlinereport.OnlineUsersRep;
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
public class OnlineUsersRepREST {

    @Autowired
    OnlineUsersRep service;

    @RequestMapping(value = "/onlineusers", method = RequestMethod.POST)
    public List<OnlineUsersRepData> query() {
        return service.getResult();
    }

}
