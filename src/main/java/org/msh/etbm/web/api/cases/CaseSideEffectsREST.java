package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.sideeffects.CaseSideEffectData;
import org.msh.etbm.services.cases.sideeffects.CaseSideEffectFormData;
import org.msh.etbm.services.cases.sideeffects.CaseSideEffectQueryParams;
import org.msh.etbm.services.cases.sideeffects.CaseSideEffectService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Mauricio on 12/08/2016.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = { Permissions.CASES_ADV_EFFECTS })
public class CaseSideEffectsREST {

    @Autowired
    CaseSideEffectService service;

    @RequestMapping(value = "/sideeffect/{id}", method = RequestMethod.GET)
    public CaseSideEffectData get(@PathVariable UUID id) {
        return service.findOne(id, CaseSideEffectData.class);
    }

    @RequestMapping(value = "/sideeffect", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_ADV_EFFECTS_EDT })
    public StandardResult create(@Valid @NotNull @RequestBody CaseSideEffectFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/sideeffect/{id}", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_ADV_EFFECTS_EDT })
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CaseSideEffectFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/sideeffect/{id}", method = RequestMethod.DELETE)
    @Authenticated(permissions = { Permissions.CASES_ADV_EFFECTS_EDT })
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/sideeffect/query", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody CaseSideEffectQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/sideeffect/form/{id}", method = RequestMethod.GET)
    public CaseSideEffectFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, CaseSideEffectFormData.class);
    }
}
