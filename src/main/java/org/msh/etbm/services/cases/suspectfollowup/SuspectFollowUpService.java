package org.msh.etbm.services.cases.suspectfollowup;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.security.ForbiddenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauricio on 18/09/2016.
 */
@Service
public class SuspectFollowUpService {

    @Autowired
    FormService formService;

    public FormInitResponse initForm (CaseClassification cla) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("tbcase", new HashMap<>());

        if (cla == null) {
            return formService.init("suspect-followup-not-tb", doc, false);
        }

        String formId = "suspect-followup-" + cla.name().toLowerCase();
        doc.put("classification", cla);
        doc.put("diagnosisType", DiagnosisType.CONFIRMED);
        return formService.init(formId, doc, false);
    }
}
