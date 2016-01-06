package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureQuery;
import org.msh.etbm.services.admin.admunits.CountryStructureRequest;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
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
 * Set of rest API to support operations on country structures
 * Created by rmemoria on 24/10/15.
 */
@RequestMapping("/api/tbl")
@RestController
@Authenticated(permissions = {Permissions.TABLE_ADMUNITS_EDT})
public class CountryStructureREST {

    @Autowired
    CountryStructureService service;


    @RequestMapping(value = "/countrystructure/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        CountryStructureData data = service.findOne(id, CountryStructureData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/countrystructure", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CountryStructureRequest req) throws BindException {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/countrystructure/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CountryStructureRequest req) throws BindException  {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/countrystructure/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) throws BindException  {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/countrystructure/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody CountryStructureQuery query) {
        return service.query(query);
    }

}
