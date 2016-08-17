package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.cases.CaseDetailedData;
import org.msh.etbm.services.cases.cases.CaseFormData;
import org.msh.etbm.services.cases.cases.CaseQueryParams;
import org.msh.etbm.services.cases.cases.CaseService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES})
public class CasesREST {

    @Autowired
    CaseService service;

    @RequestMapping(value = "/case/{id}", method = RequestMethod.GET)
    @Authenticated()
    public CaseDetailedData get(@PathVariable UUID id) {
        CaseDetailedData ret = service.findOne(id, CaseDetailedData.class);
        ret.setAlcoholExcessiveUse(true);
        ret.setAnaemia(true);
        return ret;
    }

    @RequestMapping(value = "/case", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CaseFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/case/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CaseFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/case/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/case/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody CaseQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/case/form/{id}", method = RequestMethod.GET)
    public CaseFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, CaseFormData.class);
    }

}
