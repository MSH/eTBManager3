package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.services.cases.issues.IssueFormData;
import org.msh.etbm.services.cases.issues.IssueService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 03/08/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_ISSUES})
public class IssuesREST {

    @Autowired
    IssueService service;

    @RequestMapping(value = "/issue", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody IssueFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/issue/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody IssueFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/issue/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/issue/form/{id}", method = RequestMethod.GET)
    public IssueFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, IssueFormData.class);
    }

}
