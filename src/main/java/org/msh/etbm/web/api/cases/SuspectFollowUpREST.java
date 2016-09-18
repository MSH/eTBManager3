package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by msantos on 17/9/16.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES_CLOSE})
public class SuspectFollowUpREST {

    @Autowired
    FormService formService;

    @RequestMapping(value = "/suspectfollowup/initform/{cla}", method = RequestMethod.GET)
    public FormInitResponse initForm(@PathVariable String cla) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("tbcase", new HashMap<>());

        if (cla.equals("NOT_TB")) {
            return formService.init("suspect-followup-not-tb", doc, false);
        }

        if (cla.equals("TB") || cla.equals("DRTB")) {
            String formId = "suspect-followup-" + cla.toLowerCase();
            doc.put("classification", CaseClassification.valueOf(cla));
            return formService.init(formId, doc, false);
        }

        throw new ForbiddenException("Suspect confirm classification should be TB, DRTB or NOT TB");
    }

}
