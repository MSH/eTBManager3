package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.admin.userprofiles.UserProfileDetailedData;
import org.msh.etbm.services.admin.userprofiles.UserProfileFormData;
import org.msh.etbm.services.admin.userprofiles.UserProfileQueryParams;
import org.msh.etbm.services.admin.userprofiles.UserProfileService;
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
 * CRUD API that exposes CRUD operations for user profiles
 * Created by rmemoria on 26/01/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_USERPROFILES_EDT})
public class UserProfileREST {

    @Autowired
    UserProfileService service;

    @Autowired
    FormService formService;


    /**
     * Return displayable information about a user profile
     * @param id the ID of the profile
     * @return
     */
    @RequestMapping(value = "/userprofile/{id}", method = RequestMethod.GET)
    @Authenticated()
    public UserProfileDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, UserProfileDetailedData.class);
    }

    @RequestMapping(value = "/userprofile", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody UserProfileFormData data) throws BindException {
        ServiceResult res = service.create(data);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/userprofile/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody UserProfileFormData req)  throws BindException {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/userprofile/{id}", method = RequestMethod.DELETE)
    public UUID delete(@PathVariable @NotNull UUID id) throws BindException {
        return service.delete(id).getId();
    }

    @RequestMapping(value = "/userprofile/query", method = RequestMethod.POST)
    @Authenticated
    public QueryResult query(@Valid @RequestBody UserProfileQueryParams qry) {
        return service.findMany(qry);
    }
}
