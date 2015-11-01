package org.msh.etbm.web.api.admunits;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitQuery;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
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
@Authenticated(permissions = {Permissions.ADMIN_ADMUNITS_EDT})
public class AdminUnitsREST {

    @Autowired
    AdminUnitService adminUnitService;

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.GET)
    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
    public StandardResult get(@PathVariable UUID id) {
        AdminUnitData data = adminUnitService.findOne(id, AdminUnitData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/adminunit", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody AdminUnitRequest req) {
        ServiceResult res = adminUnitService.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody AdminUnitRequest req) {
        ServiceResult res = adminUnitService.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = adminUnitService.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/adminunit/query", method = RequestMethod.POST)
    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
    public QueryResult query(@Valid @RequestBody @NotNull AdminUnitQuery query) {
        return adminUnitService.findMany(query);
    }
}
