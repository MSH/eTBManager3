package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.services.admin.sources.SourceData;
import org.msh.etbm.services.admin.sources.SourceFormData;
import org.msh.etbm.services.admin.sources.SourceQueryParams;
import org.msh.etbm.services.admin.sources.SourceService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.InstanceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Rest api to handle CRUD operations on medicine sources
 * <p>
 * Created by rmemoria on 11/11/15.
 */
@RequestMapping("/api/tbl")
@RestController
@Authenticated(permissions = {Permissions.TABLE_SOURCES_EDT}, instanceType = InstanceType.SERVER_MODE)
public class SourcesREST {

    private static final String API_PREFIX = "/source";

    @Autowired
    SourceService service;

    @Autowired
    FormRequestService formRequestService;


    @RequestMapping(value = API_PREFIX + "/{id}", method = RequestMethod.GET)
    @Authenticated()
    public SourceData get(@PathVariable UUID id) {
        return service.findOne(id, SourceData.class);
    }

    @RequestMapping(value = API_PREFIX + "/form/{id}", method = RequestMethod.GET)
    @Authenticated()
    public SourceFormData getFormData(@PathVariable UUID id) {
        return service.findOne(id, SourceFormData.class);
    }

    @RequestMapping(value = API_PREFIX, method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody SourceFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = API_PREFIX + "/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody SourceFormData req) {
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
    public QueryResult query(@Valid @RequestBody SourceQueryParams query) {
        return service.findMany(query);
    }

}
