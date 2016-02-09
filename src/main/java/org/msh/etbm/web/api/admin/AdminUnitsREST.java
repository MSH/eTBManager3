package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.admin.admunits.AdminUnitDetailedData;
import org.msh.etbm.services.admin.admunits.AdminUnitFormData;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
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
 * REST API to handle administrative unit CRUD operations
 *
 * Created by rmemoria on 21/10/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_ADMUNITS_EDT})
public class AdminUnitsREST {

    @Autowired
    AdminUnitService service;

    @Autowired
    FormService formService;

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.GET)
    @Authenticated()
    public AdminUnitDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, AdminUnitDetailedData.class);
    }

    @RequestMapping(value = "/adminunit/form/{id}", method = RequestMethod.GET)
    @Authenticated
    public AdminUnitFormData getFormData(@PathVariable UUID id) {
        return service.findOne(id, AdminUnitFormData.class);
    }

    @RequestMapping(value = "/adminunit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody AdminUnitFormData req)  throws BindException {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody AdminUnitFormData req) throws BindException {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.DELETE)
    public UUID delete(@PathVariable @NotNull UUID id) throws BindException {
        return service.delete(id).getId();
    }

    @RequestMapping(value = "/adminunit/query", method = RequestMethod.POST)
    @Authenticated(permissions = {Permissions.TABLE_ADMUNITS})
    public QueryResult query(@Valid @RequestBody @NotNull AdminUnitQueryParams query) {
        return service.findMany(query);
    }
}
