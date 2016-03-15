package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.sessionreport.UserSessionRepQueryParams;
import org.msh.etbm.services.admin.sessionreport.UserSessionRepService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by msantos on 10/3/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_USERSESSIONS})
public class UserSessionRepREST {

    @Autowired
    UserSessionRepService service;

    @RequestMapping(value = "/dailyusersession", method = RequestMethod.POST)
    public QueryResult queryByDay(@Valid @RequestBody UserSessionRepQueryParams query) {
        return service.getResultByDay(query);
    }

    @RequestMapping(value = "/usersession", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody UserSessionRepQueryParams query) {
        return service.getResult(query);
    }
}
