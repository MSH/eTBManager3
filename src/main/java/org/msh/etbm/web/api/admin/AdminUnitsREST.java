package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormResponse;
import org.msh.etbm.commons.forms.FormsService;
import org.msh.etbm.services.admin.admunits.AdminUnitDetailedData;
import org.msh.etbm.services.admin.admunits.AdminUnitFormData;
import org.msh.etbm.services.admin.admunits.AdminUnitQueryParams;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.services.admin.sources.SourceFormData;
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
    FormsService formsService;

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        AdminUnitDetailedData data = service.findOne(id, AdminUnitDetailedData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/adminunit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody AdminUnitFormData req)  throws BindException {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/form", method = RequestMethod.POST)
    public FormResponse getFormData(@Valid @NotNull @RequestBody FormRequest req) {
        return formsService.initForm(req, service, SourceFormData.class);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody AdminUnitFormData req) throws BindException {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) throws BindException {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/query", method = RequestMethod.POST)
    @Authenticated(permissions = {Permissions.TABLE_ADMUNITS})
    public QueryResult query(@Valid @RequestBody @NotNull AdminUnitQueryParams query) {
        return service.findMany(query);
    }
}
