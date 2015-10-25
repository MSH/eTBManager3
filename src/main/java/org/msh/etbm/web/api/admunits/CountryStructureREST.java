package org.msh.etbm.web.api.admunits;

import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureRequest;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 24/10/15.
 */
@RequestMapping("/api/tbl")
@RestController
@Authenticated(permissions = {Permissions.ADMIN_ADMUNITS_EDT})
public class CountryStructureREST {

    @Autowired
    CountryStructureService service;


    @RequestMapping(value = "/countrystructure/{id}", method = RequestMethod.GET)
    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
    public CountryStructureData get(@PathVariable UUID id) {
        return service.get(id);
    }

    @RequestMapping(value = "/countrystructure", method = RequestMethod.POST)
    public UUID create(@Valid @NotNull @RequestBody CountryStructureRequest req) {
        return service.create(req);
    }

    @RequestMapping(value = "/countrystructure/{id}", method = RequestMethod.POST)
    public UUID update(@PathVariable UUID id, @Valid @NotNull @RequestBody CountryStructureRequest req) {
        service.update(id, req);
        return id;
    }

    @RequestMapping(value = "/countrystructure/del/{id}", method = RequestMethod.POST)
    public UUID delete(@PathVariable @NotNull UUID id) {
        service.delete(id);
        return id;
    }

    @RequestMapping(value = "/countrystructure", method = RequestMethod.POST)
    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
    public List<CountryStructureData> query() {
        return service.query();
    }

}
