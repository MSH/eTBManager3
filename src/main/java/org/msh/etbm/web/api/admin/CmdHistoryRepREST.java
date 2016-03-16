package org.msh.etbm.web.api.admin;

import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepData;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepQueryParams;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by msantos on 15/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_CMDHISTORY})
public class CmdHistoryRepREST {

    @Autowired
    CmdHistoryRepService service;

    @RequestMapping(value = "/cmdhistory", method = RequestMethod.POST)
    public List<CmdHistoryRepData> query(@Valid @RequestBody CmdHistoryRepQueryParams query) {
        System.out.println("iniDate: " + query.getIniDate());
        System.out.println("endDate: " + query.getEndDate());
        System.out.println("Action: " + query.getAction());
        System.out.println("userId: " + query.getUserId());
        System.out.println("Type: " + query.getType());
        System.out.println("adminUnitId: " + query.getAdminUnitId());
        System.out.println("SearchKey: " + query.getSearchKey());

        return null;
    }

    @RequestMapping(value = "/todaycmdhistory", method = RequestMethod.POST)
    public List<CmdHistoryRepData> todayResult() {
        System.out.println("hey oh! Lets Go!");

        return null;
    }

}
