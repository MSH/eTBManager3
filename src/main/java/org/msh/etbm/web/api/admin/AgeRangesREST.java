package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.ageranges.AgeRangeData;
import org.msh.etbm.services.admin.ageranges.AgeRangeFormData;
import org.msh.etbm.services.admin.ageranges.AgeRangeRequest;
import org.msh.etbm.services.admin.ageranges.AgeRangeService;
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
 *
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
    public UUID delete(@PathVariable @NotNull UUID id) {
        return service.delete(id).getId();
    }

    @RequestMapping(value = "/agerange/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query() {
        return service.findMany(null);
    }

    @RequestMapping(value = "/agerange/form/{id}", method = RequestMethod.GET)
    public AgeRangeFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, AgeRangeFormData.class);
    }

}
