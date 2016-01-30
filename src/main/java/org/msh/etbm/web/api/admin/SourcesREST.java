package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormResponse;
import org.msh.etbm.commons.forms.FormsService;
import org.msh.etbm.services.admin.sources.SourceData;
import org.msh.etbm.services.admin.sources.SourceFormData;
import org.msh.etbm.services.admin.sources.SourceQueryParams;
import org.msh.etbm.services.admin.sources.SourceService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Rest api to handle CRUD operations on medicine sources
 *
 * Created by rmemoria on 11/11/15.
 */
@RequestMapping("/api/tbl")
@RestController
@Authenticated(permissions = {Permissions.TABLE_SOURCES_EDT})
public class SourcesREST {

    private static final String API_PREFIX = "/source";

    @Autowired
    SourceService service;

    @Autowired
    FormsService formsService;


    @RequestMapping(value = API_PREFIX + "/{id}", method = RequestMethod.GET)
    @Authenticated()
    public SourceData get(@PathVariable UUID id) {
        return service.findOne(id, SourceData.class);
    }

    @RequestMapping(value = API_PREFIX + "/form", method = RequestMethod.POST)
    public FormResponse getFormData(@Valid @NotNull @RequestBody FormRequest req) {
        return formsService.initForm(req, service, SourceFormData.class);
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
    public UUID delete(@PathVariable @NotNull UUID id) {
        return service.delete(id).getId();
    }

    @RequestMapping(value = API_PREFIX + "/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody SourceQueryParams query) {
        return service.findMany(query);
    }

}
