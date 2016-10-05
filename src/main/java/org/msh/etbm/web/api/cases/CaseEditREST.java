package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.services.cases.cases.CaseEditService;
import org.msh.etbm.services.cases.cases.data.CaseEditFormData;
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
public class CaseEditREST {

    @Autowired
    CaseEditService service;

    @RequestMapping(value = "/edit/form/{id}")
    public FormInitResponse initForm(@PathVariable @NotNull UUID id) {
        return service.initForm(id);
    }

    @RequestMapping(value = "/edit/save", method = RequestMethod.POST)
    public StandardResult saveEdit(@Valid @NotNull @RequestBody CaseEditFormData req) {
        return service.save(req);
    }
}
