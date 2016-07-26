package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.tag.CaseTagsFormData;
import org.msh.etbm.services.cases.tag.ManualTagsCasesServices;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Mauricio on 25/07/2016.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES_TAG})
public class CaseTagsREST {

    @Autowired
    ManualTagsCasesServices service;

    @RequestMapping(value = "/tags", method = RequestMethod.POST)
    public StandardResult closeCase(@Valid @NotNull @RequestBody CaseTagsFormData req) {
        service.updateTags(req);
        return new StandardResult(null, null, true);
    }

}
