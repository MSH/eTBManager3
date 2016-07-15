package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.regimens.RegimenData;
import org.msh.etbm.services.admin.regimens.RegimenFormData;
import org.msh.etbm.services.admin.regimens.RegimenQueryParams;
import org.msh.etbm.services.admin.regimens.RegimenService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST API controller to expose CRUD operations in a medicine regimen
 * <p>
 * Created by rmemoria on Jan 6th, 2015.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_REGIMENS_EDT})
public class RegimensREST {

    private static final String API_PREFIX = "/regimen";

    @Autowired
    RegimenService service;

    @RequestMapping(value = API_PREFIX + "/{id}", method = RequestMethod.GET)
    @Authenticated()
    public RegimenData get(@PathVariable UUID id) {
        return service.findOne(id, RegimenData.class);
    }

    @RequestMapping(value = API_PREFIX, method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody RegimenFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = API_PREFIX + "/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody RegimenFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = API_PREFIX + "/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = API_PREFIX + "/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody RegimenQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = API_PREFIX + "/form/{id}", method = RequestMethod.GET)
    public RegimenFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, RegimenFormData.class);
    }

}
