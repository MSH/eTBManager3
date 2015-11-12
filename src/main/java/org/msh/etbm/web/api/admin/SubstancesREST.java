package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.admin.sources.SourceData;
import org.msh.etbm.services.admin.sources.SourceQuery;
import org.msh.etbm.services.admin.sources.SourceRequest;
import org.msh.etbm.services.admin.substances.SubstanceData;
import org.msh.etbm.services.admin.substances.SubstanceQuery;
import org.msh.etbm.services.admin.substances.SubstanceRequest;
import org.msh.etbm.services.admin.substances.SubstanceService;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.msh.etbm.web.api.authentication.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by rmemoria on 12/11/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.ADMIN_SUBSTANCES_EDT})
public class SubstancesREST {

    @Autowired
    SubstanceService service;


    @RequestMapping(value = "/substance/{id}", method = RequestMethod.GET)
    @Authenticated()
    public StandardResult get(@PathVariable UUID id) {
        SubstanceData data = service.findOne(id, SubstanceData.class);
        return new StandardResult(data, null, data != null);
    }

    @RequestMapping(value = "/substance", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody SubstanceRequest req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/substance/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody SubstanceRequest req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/substance/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        ServiceResult res = service.delete(id);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/substance/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody SubstanceQuery query) {
        return service.findMany(query);
    }


}
