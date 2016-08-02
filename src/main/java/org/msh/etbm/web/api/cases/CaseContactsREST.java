package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.db.entities.CaseContact;
import org.msh.etbm.services.cases.contacts.CaseContactData;
import org.msh.etbm.services.cases.contacts.CaseContactFormData;
import org.msh.etbm.services.cases.contacts.CaseContactQueryParams;
import org.msh.etbm.services.cases.contacts.CaseContactService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_TBCONTACT})
public class CaseContactsREST {

    @Autowired
    CaseContactService service;

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.GET)
    @Authenticated()
    public CaseContactData get(@PathVariable UUID id) {
        return service.findOne(id, CaseContactData.class);
    }

    @RequestMapping(value = "/contact", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CaseContactFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CaseContactFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/contact/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/contact/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody CaseContactQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/contact/form/{id}", method = RequestMethod.GET)
    public CaseContactFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, CaseContactFormData.class);
    }

}
