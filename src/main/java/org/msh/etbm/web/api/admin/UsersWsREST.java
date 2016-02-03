package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.EntityServiceImpl;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormResponse;
import org.msh.etbm.commons.forms.FormsService;
import org.msh.etbm.services.admin.userprofiles.UserProfileFormData;
import org.msh.etbm.services.admin.usersws.UserWsDetailedData;
import org.msh.etbm.services.admin.usersws.UserWsFormData;
import org.msh.etbm.services.admin.usersws.UserWsQueryParams;
import org.msh.etbm.services.admin.usersws.UserWsService;
import org.msh.etbm.services.permissions.Permissions;
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
    FormsService formsService;


    /**
     * Return displayable information about a user profile
     * @param id the ID of the profile
     * @return
     */
    @RequestMapping(value = PREFIX + "/{id}", method = RequestMethod.GET)
    @Authenticated()
    public UserWsDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, UserWsDetailedData.class);
    }

    /**
     * User profile data to initialize a form
     * @param req
     * @return
     */
    @RequestMapping(value = PREFIX + "/form", method = RequestMethod.POST)
    public FormResponse initForm(@Valid @NotNull @RequestBody FormRequest req) {
        return formsService.initForm(req, (EntityServiceImpl)service, UserWsFormData.class);
    }

    @RequestMapping(value = PREFIX, method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody UserWsFormData data) throws BindException {
        ServiceResult res = service.create(data);
        return new StandardResult(res);
    }

    @RequestMapping(value = PREFIX + "/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody UserProfileFormData req)  throws BindException {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = PREFIX + "/{id}", method = RequestMethod.DELETE)
    public UUID delete(@PathVariable @NotNull UUID id) throws BindException {
        return service.delete(id).getId();
    }

    @RequestMapping(value = PREFIX + "/query", method = RequestMethod.POST)
    @Authenticated
    public QueryResult query(@Valid @RequestBody UserWsQueryParams qry) {
        return service.findMany(qry);
    }

}
