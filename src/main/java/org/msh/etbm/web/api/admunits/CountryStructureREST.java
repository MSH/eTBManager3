package org.msh.etbm.web.api.admunits;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureRequest;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
        return service.findOne(id, CountryStructureData.class);
    }

    @RequestMapping(value = "/countrystructure", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CountryStructureRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/countrystructure/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CountryStructureRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/countrystructure/del/{id}", method = RequestMethod.POST)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

//    @RequestMapping(value = "/countrystructures", method = RequestMethod.POST)
//    @Authenticated(permissions = {Permissions.ADMIN_ADMUNITS})
//    public List<CountryStructureData> query() {
//        return service.query();
//    }

}
