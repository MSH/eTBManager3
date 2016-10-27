package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.comorbidity.CaseComorbiditiesData;
import org.msh.etbm.services.cases.comorbidity.CaseComorbiditiesService;
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
    CaseComorbiditiesService service;

    @RequestMapping(value = "/comorbidity/{id}", method = RequestMethod.POST)
    public StandardResult updateComorbidity(@PathVariable @NotNull UUID id, @Valid @NotNull @RequestBody CaseComorbiditiesData req) {
        service.update(id, req);
        return StandardResult.createSuccessResult();
    }
}
