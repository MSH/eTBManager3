package org.msh.etbm.web.api.admunits;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.services.admin.admunits.AdminUnitData;
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
    public AdminUnitData get(@PathVariable UUID id) {
        return adminUnitService.findOne(id, AdminUnitData.class);
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

//    @RequestMapping(value = "/adminunits", method = RequestMethod.POST)
//    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
//    public List<AdminUnitData> query(@Valid @RequestBody @NotNull AdminUnitQuery query) {
//        return adminUnitService.query(query);
//    }
}
