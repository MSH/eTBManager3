package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepQueryParams;
import org.msh.etbm.services.admin.cmdhisotryrep.CmdHistoryRepService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.InstanceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by msantos on 15/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_CMDHISTORY}, instanceType = InstanceType.SERVER_MODE)
public class CmdHistoryRepREST {

    @Autowired
    CmdHistoryRepService service;

    @RequestMapping(value = "/cmdhistory", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody CmdHistoryRepQueryParams query) {
        return service.getResult(query);
    }

    @RequestMapping(value = "/todaycmdhistory", method = RequestMethod.POST)
    public QueryResult todayResult() {
        CmdHistoryRepQueryParams query = new CmdHistoryRepQueryParams();
        query.setIniDate(DateUtils.getDate());
        return service.getResult(query);
    }

}
