package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.admunits.*;
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
    AdminUnitService adminUnitService;

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        AdminUnitDetailedData data = adminUnitService.findOne(id, AdminUnitDetailedData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/adminunit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody AdminUnitRequest req)  throws BindException {
        ServiceResult res = adminUnitService.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody AdminUnitRequest req) throws BindException {
        ServiceResult res = adminUnitService.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) throws BindException {
        ServiceResult res = adminUnitService.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/query", method = RequestMethod.POST)
    @Authenticated(permissions = {Permissions.TABLE_ADMUNITS})
    public QueryResult query(@Valid @RequestBody @NotNull AdminUnitQuery query) {
        return adminUnitService.findMany(query);
    }
}
