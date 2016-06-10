package org.msh.etbm.web.api.admin;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.admin.substances.SubstanceData;
import org.msh.etbm.services.admin.substances.SubstanceFormData;
import org.msh.etbm.services.admin.substances.SubstanceQueryParams;
import org.msh.etbm.services.admin.substances.SubstanceService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Rest API to handle CRUD operations in substances
 *
 * Created by rmemoria on 12/11/15.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.TABLE_SUBSTANCES_EDT})
public class SubstancesREST {

    @Autowired
    SubstanceService service;

    @Autowired
    FormService formService;


    @RequestMapping(value = "/substance/{id}", method = RequestMethod.GET)
    @Authenticated()
    public SubstanceData get(@PathVariable UUID id) {
        return service.findOne(id, SubstanceData.class);
    }

    @RequestMapping(value = "/substance/form/{id}", method = RequestMethod.GET)
    @Authenticated()
    public SubstanceFormData getFormData(@PathVariable UUID id) {
        return service.findOne(id, SubstanceFormData.class);
    }

    @RequestMapping(value = "/substance", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody SubstanceFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/substance/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody SubstanceFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/substance/{id}", method = RequestMethod.DELETE)
    public UUID delete(@PathVariable @NotNull UUID id) {
        return service.delete(id).getId();
    }

    @RequestMapping(value = "/substance/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody SubstanceQueryParams query) {
        return service.findMany(query);
    }


}
