package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.services.cases.issues.followup.IssueFollowUpFormData;
import org.msh.etbm.services.cases.issues.followup.IssueFollowUpService;
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
@Authenticated(permissions = {Permissions.CASES_ANSWER_ISSUE})
public class IssueFollowUpsREST {

    @Autowired
    IssueFollowUpService service;

    @RequestMapping(value = "/issuefollowup", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody IssueFollowUpFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/issuefollowup/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody IssueFollowUpFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/issuefollowup/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/issuefollowup/form/{id}", method = RequestMethod.GET)
    public IssueFollowUpFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, IssueFollowUpFormData.class);
    }

}
