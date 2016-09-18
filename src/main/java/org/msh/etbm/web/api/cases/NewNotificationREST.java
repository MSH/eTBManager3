package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.cases.cases.*;
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
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES})
public class NewNotificationREST {

    @Autowired
    CaseService service;

    @Autowired
    NewNotificationService newNotificationService;

    @Autowired
    FormService formService;

    @RequestMapping(value = "/newnotif/form")
    public FormInitResponse initForm(@Valid @NotNull @RequestBody CaseInitFormReq req) {

        // mount case data
        Map<String, Object> caseData = new HashMap<>();
        caseData.put("diagnosisType", req.getDiagnosisType().name());
        caseData.put("classification", req.getCaseClassification().name());

        // mount doc
        Map<String, Object> doc = new HashMap<>();
        doc.put("tbcase", caseData);
        doc.put("patient", new HashMap<>());

        // generate form id
        String formid = "newnotif-";
        formid = formid.concat(req.getDiagnosisType().name().toLowerCase()).concat("-");
        formid = formid.concat(req.getCaseClassification().name().toLowerCase());

        return formService.init(formid, doc, false);
    }

    @RequestMapping(value = "/newnotif", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CaseFormData req) {
        return newNotificationService.create(req);
    }
}
