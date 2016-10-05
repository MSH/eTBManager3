package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.services.cases.cases.NewNotificationService;
import org.msh.etbm.services.cases.cases.data.NewNotificationFormData;
import org.msh.etbm.services.cases.cases.data.NewNotificationInitFormReq;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    public FormInitResponse initForm(@Valid @NotNull @RequestBody NewNotificationInitFormReq req) {

        if (req == null || req.getCaseClassification() == null || req.getDiagnosisType() == null) {
            throw new InvalidArgumentException("Classificaton and diagnosisType must be informed.");
        }

        return newNotificationService.initForm(req.getCaseClassification(), req.getDiagnosisType());
    }

    @RequestMapping(value = "/newnotif", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody NewNotificationFormData req) {
        return newNotificationService.save(req);
    }
}
