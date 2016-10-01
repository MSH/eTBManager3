package org.msh.etbm.services.cases.suspectfollowup;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mauricio on 18/09/2016.
 */
@Service
public class SuspectFollowUpService {

    @Autowired
    FormService formService;

    @Autowired
    ModelDAOFactory factory;

    @Autowired
    EntityManager entityManager;

    // TODO: registrar commandlog

    public FormInitResponse initForm (CaseClassification cla) {
        Map<String, Object> doc = new HashMap<>();
        Map<String, Object> caseData = new HashMap<>();

        doc.put("tbcase", caseData);

        if (cla == null) {
            return formService.init("suspect-followup/not-tb", doc, false);
        }

        String formId = "suspect-followup/" + cla.name().toLowerCase();
        caseData.put("classification", cla);
        return formService.init(formId, doc, false);
    }

    @Transactional
    public StandardResult save(SuspectFollowUpData data) {
        // validate if it is a suspect
        TbCase tbcase = entityManager.find(TbCase.class, data.getTbcaseId());

        if (!tbcase.getDiagnosisType().equals(DiagnosisType.SUSPECT)) {
            throw new EntityValidationException(tbcase, "diagnosisType", "TbCase must be a suspect", null);
        }

        Map caseData = (Map) data.getDoc().get("tbcase");

        if (caseData.get("classification") != null) {
            caseData.put("diagnosisType", DiagnosisType.CONFIRMED);
        } else {
            caseData.put("state", CaseState.CLOSED);
        }

        ModelDAO dao = factory.create("tbcase");
        ModelDAOResult res = dao.update(tbcase.getId(), caseData);

        if (res.getErrors() != null) {
            throw new EntityValidationException(res.getErrors());
        }

        return new StandardResult(res.getId().toString(), null, true);
    }
}
