package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.services.cases.cases.CaseService;
import org.msh.etbm.services.cases.comorbidity.ComorbidityFormData;
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
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES})
public class CaseComorbiditiesREST {

    @Autowired
    CaseService service;

    @RequestMapping(value = "/comorbidity/{id}", method = RequestMethod.POST)
    public StandardResult updateComorbidity(@PathVariable UUID id, @Valid @NotNull @RequestBody ComorbidityFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }
}
