package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.services.cases.comments.CaseCommentFormData;
import org.msh.etbm.services.cases.comments.CaseCommentService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 27/7/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_COMMENTS})
public class CaseCommentsREST {

    @Autowired
    CaseCommentService service;

    @RequestMapping(value = "/casecomment", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CaseCommentFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/casecomment/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CaseCommentFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/casecomment/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/casecomment/form/{id}", method = RequestMethod.GET)
    public CaseCommentFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, CaseCommentFormData.class);
    }

}
