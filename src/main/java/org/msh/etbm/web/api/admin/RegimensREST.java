package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.regimens.RegimenData;
import org.msh.etbm.services.admin.regimens.RegimenQueryParams;
import org.msh.etbm.services.admin.regimens.RegimenRequest;
import org.msh.etbm.services.admin.regimens.RegimenService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST API controller to expose CRUD operations in a medicine regimen
 *
 * Created by rmemoria on Jan 6th, 2015.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_REGIMENS_EDT})
public class RegimensREST {

    @Autowired
    RegimenService service;


    @RequestMapping(value = "/regimen/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        RegimenData data = service.findOne(id, RegimenData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/regimen", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody RegimenRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/regimen/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody RegimenRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/regimen/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/regimen/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody RegimenQueryParams query) {
        return service.findMany(query);
    }

}
