package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.errorlogrep.ErrorLogRepQueryParams;
import org.msh.etbm.services.admin.errorlogrep.ErrorLogRepServiceImpl;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by msantos on 05/7/16.
 */
@RestController
@RequestMapping("/api/admin/rep")
@Authenticated(permissions = {Permissions.ADMIN_REP_ERRORLOG})
public class ErrorLogRepREST {

    @Autowired
    ErrorLogRepServiceImpl service;

    @RequestMapping(value = "/errorlog", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody ErrorLogRepQueryParams query) {
        return service.getResult(query);
    }

    @RequestMapping(value = "/todayerrorlog", method = RequestMethod.POST)
    public QueryResult todayResult() {
        ErrorLogRepQueryParams query = new ErrorLogRepQueryParams();
        query.setIniDate(DateUtils.getDate());
        return service.getResult(query);
    }

}
