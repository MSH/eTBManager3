package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.services.admin.usersws.UserWsQueryParams;
import org.msh.etbm.services.admin.usersws.UserWsService;
import org.msh.etbm.services.admin.usersws.data.UserWsChangePwdFormData;
import org.msh.etbm.services.admin.usersws.data.UserWsDetailedData;
import org.msh.etbm.services.admin.usersws.data.UserWsFormData;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by rmemoria on 26/1/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_USERS_EDT})
public class UsersWsREST {

    private static final String PREFIX = "/userws";

    @Autowired
    UserWsService service;

    @Autowired
    FormRequestService formRequestService;

    /**
     * Return displayable information about a user profile
     *
     * @param id the ID of the profile
     * @return
     */
    @RequestMapping(value = PREFIX + "/{id}", method = RequestMethod.GET)
    @Authenticated()
    public UserWsDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, UserWsDetailedData.class);
    }


    @RequestMapping(value = PREFIX + "/form/{id}", method = RequestMethod.GET)
    @Authenticated()
    public UserWsFormData getFormData(@PathVariable UUID id) {
        return service.findOne(id, UserWsFormData.class);
    }


    @RequestMapping(value = PREFIX, method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody UserWsFormData data) throws BindException {
        ServiceResult res = service.create(data);
        return new StandardResult(res);
    }

    @RequestMapping(value = PREFIX + "/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody UserWsFormData req) throws BindException {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = PREFIX + "/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) throws BindException {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = PREFIX + "/query", method = RequestMethod.POST)
    @Authenticated
    public QueryResult query(@Valid @RequestBody UserWsQueryParams qry) {
        return service.findMany(qry);
    }

    @RequestMapping(value = PREFIX + "/updatepwd", method = RequestMethod.POST)
    @Authenticated
    public void updatePassword(@Valid @RequestBody UserWsChangePwdFormData data) {
        service.changePassword(data);
    }

    @RequestMapping(value = PREFIX + "/resetpwd", method = RequestMethod.POST)
    @Authenticated
    public void resetPassword(@Valid @RequestBody UserWsChangePwdFormData data) {
        service.sendPwdResetLink(data);
    }
}
