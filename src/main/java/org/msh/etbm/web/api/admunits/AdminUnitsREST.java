package org.msh.etbm.web.api.admunits;

import org.msh.etbm.services.admin.admunits.AdminUnitData;
import org.msh.etbm.services.admin.admunits.AdminUnitQuery;
import org.msh.etbm.services.admin.admunits.AdminUnitRequest;
import org.msh.etbm.services.admin.admunits.AdminUnitService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
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

    @RequestMapping(value = "/admunit/{id}", method = RequestMethod.GET)
    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
    public AdminUnitData get(@PathVariable UUID id) {
        return adminUnitService.get(id);
    }

    @RequestMapping(value = "/admunit", method = RequestMethod.POST)
    public UUID create(@Valid @NotNull @RequestBody AdminUnitRequest req) {
        return adminUnitService.create(req);
    }

    @RequestMapping(value = "/admunit/{id}", method = RequestMethod.POST)
    public UUID update(@PathVariable UUID id, @Valid @NotNull @RequestBody AdminUnitRequest req) {
        adminUnitService.update(id, req);
        return id;
    }

    @RequestMapping(value = "/admunit/del/{id}", method = RequestMethod.POST)
    public UUID delete(@PathVariable @NotNull UUID id) {
        adminUnitService.delete(id);
        return id;
    }

    @RequestMapping(value = "/adminunits", method = RequestMethod.POST)
    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
    public List<AdminUnitData> query(@Valid @RequestBody @NotNull AdminUnitQuery query) {
        return adminUnitService.query(query);
    }
}
