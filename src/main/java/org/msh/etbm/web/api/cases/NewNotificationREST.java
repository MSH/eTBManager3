package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.cases.cases.*;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES})
public class NewNotificationREST {

    @Autowired
    NewNotificationService newNotificationService;

    @RequestMapping(value = "/newnotif/form")
    public FormInitResponse initForm(@Valid @NotNull @RequestBody CaseInitFormReq req) {

        if (req == null || req.getCaseClassification() == null || req.getDiagnosisType() == null) {
            throw new InvalidArgumentException("Classificaton and diagnosisType must be informed.");
        }

        return newNotificationService.initForm(req.getCaseClassification(), req.getDiagnosisType());
    }

    @RequestMapping(value = "/newnotif", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CaseFormData req) {
        return newNotificationService.save(req);
    }
}
