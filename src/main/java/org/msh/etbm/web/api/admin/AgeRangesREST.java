package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.ageranges.*;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST Interface to handle age range CRUD operations
 * <p>
 * Created by rmemoria on 6/1/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_AGERANGES_EDT})
public class AgeRangesREST {

    @Autowired
    AgeRangeService service;


    @RequestMapping(value = "/agerange/{id}", method = RequestMethod.GET)
    @Authenticated()
    public AgeRangeData get(@PathVariable UUID id) {
        return service.findOne(id, AgeRangeData.class);
    }

    @RequestMapping(value = "/agerange", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody AgeRangeRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/agerange/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody AgeRangeRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/agerange/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/agerange/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody AgeRangesQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/agerange/form/{id}", method = RequestMethod.GET)
    public AgeRangeFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, AgeRangeFormData.class);
    }

}
