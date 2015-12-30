package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.sources.SourceData;
import org.msh.etbm.services.admin.sources.SourceQuery;
import org.msh.etbm.services.admin.sources.SourceRequest;
import org.msh.etbm.services.admin.sources.SourceService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.services.permissions.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by rmemoria on 11/11/15.
 */
@RequestMapping("/api/tbl")
@RestController
@Authenticated(permissions = {Permissions.TABLE_SOURCES_EDT})
public class SourcesREST {

    @Autowired
    SourceService service;


    @RequestMapping(value = "/source/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        SourceData data = service.findOne(id, SourceData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/source", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody SourceRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/source/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody SourceRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/source/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/source/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody SourceQuery query) {
        return service.findMany(query);
    }

}
