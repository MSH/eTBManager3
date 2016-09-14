package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.casevalidate.CaseValidateService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES})
public class CaseValidateREST {

    @Autowired
    CaseValidateService service;

    @RequestMapping(value = "/validate/{id}", method = RequestMethod.GET)
    public StandardResult get(@PathVariable UUID id) {
        // update tbcase
        service.validateCase(id);
        return new StandardResult(null, null, true);
    }
}
