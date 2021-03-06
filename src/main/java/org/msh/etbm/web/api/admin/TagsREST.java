package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.admin.tags.TagFormData;
import org.msh.etbm.services.admin.tags.TagQueryParams;
import org.msh.etbm.services.admin.tags.TagService;
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
 * Created by rmemoria on 6/1/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_TAGS_EDT}, instanceType = InstanceType.SERVER_MODE)
public class TagsREST {

    @Autowired
    TagService service;

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET)
    public TagData get(@PathVariable UUID id) {
        return service.findOne(id, TagData.class);
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody TagFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody TagFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/tag/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody TagQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/tag/form/{id}", method = RequestMethod.GET)
    public TagFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, TagFormData.class);
    }

}
