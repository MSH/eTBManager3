package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;
import org.msh.etbm.services.admin.admunits.data.AdminUnitFormData;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST API to handle administrative unit CRUD operations
 * <p>
 * Created by rmemoria on 21/10/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_ADMUNITS_EDT})
public class AdminUnitsREST {

    @Autowired
    AdminUnitService service;

    @Autowired
    FormRequestService formRequestService;

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.GET)
    @Authenticated()
    public AdminUnitData get(@PathVariable UUID id) {
        return service.findOne(id, AdminUnitData.class);
    }

    @RequestMapping(value = "/adminunit/form/{id}", method = RequestMethod.GET)
    @Authenticated
    public AdminUnitFormData getFormData(@PathVariable UUID id) {
        return service.findOne(id, AdminUnitFormData.class);
    }

    @RequestMapping(value = "/adminunit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody AdminUnitFormData req) throws BindException {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody AdminUnitFormData req) throws BindException {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) throws BindException {
        service.delete(id);
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/adminunit/query", method = RequestMethod.POST, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Authenticated(permissions = {Permissions.TABLE_ADMUNITS})
    public QueryResult query(@Valid @RequestBody @NotNull AdminUnitQueryParams query) {
        return service.findMany(query);
    }
}
