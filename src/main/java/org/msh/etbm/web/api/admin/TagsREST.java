package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.regimens.RegimenData;
import org.msh.etbm.services.admin.regimens.RegimenQuery;
import org.msh.etbm.services.admin.regimens.RegimenRequest;
import org.msh.etbm.services.admin.regimens.RegimenService;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.admin.tags.TagQuery;
import org.msh.etbm.services.admin.tags.TagRequest;
import org.msh.etbm.services.admin.tags.TagService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
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
@Authenticated(permissions = {Permissions.TABLE_REGIMENS_EDT})
public class TagsREST {

    @Autowired
    TagService service;


    @RequestMapping(value = "/tag/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        TagData data = service.findOne(id, TagData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/tag", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody TagRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody TagRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/tag/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/tag/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody TagQuery query) {
        return service.findMany(query);
    }

}
