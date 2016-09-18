package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.cases.cases.*;
import org.msh.etbm.services.cases.comorbidity.ComorbidityFormData;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES})
public class CasesREST {

    @Autowired
    CaseService service;

    @Autowired
    FormService formService;

    @RequestMapping(value = "/case/{id}", method = RequestMethod.GET)
    public CaseDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, CaseDetailedData.class);
    }

    @RequestMapping(value = "/case/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }
}
