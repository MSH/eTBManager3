package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.db.enums.InfectionSite;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mauricio on 05/09/2016.
 */
@Service
public class NewNotificationService {

    @Autowired
    ModelDAOFactory factory;

    @Autowired
    FormService formService;

    // TODO: registrar commandlog

    public FormInitResponse initForm (CaseClassification cla, DiagnosisType diag){
        if (cla == null || diag == null) {
            return null;
        }

        // mount case data
        Map<String, Object> caseData = new HashMap<>();
        caseData.put("diagnosisType", diag.name());
        caseData.put("classification", cla.name());

        // mount doc
        Map<String, Object> doc = new HashMap<>();
        doc.put("tbcase", caseData);
        doc.put("patient", new HashMap<>());

        // generate form id
        String formid = "newnotif-";
        formid = formid.concat(diag.name().toLowerCase()).concat("-");
        formid = formid.concat(cla.name().toLowerCase());

        return formService.init(formid, doc, false);
    }

    @Transactional
    public StandardResult save(CaseFormData data) {
        ModelDAO patientDao = factory.create("patient");
        ModelDAOResult resPatient = patientDao.insert((Map) data.getDoc().get("patient"));

        if (resPatient.getErrors() != null) {
            throw new EntityValidationException(resPatient.getErrors());
        }

        // prepare case data
        Map caseData = (Map)data.getDoc().get("tbcase");
        caseData.put("patient", resPatient.getId());
        caseData.put("state", CaseState.NOT_ONTREATMENT);
        caseData.put("movedToIndividualized", false);
        caseData.put("validated", false);
        caseData.put("movedSecondLineTreatment", false);
        caseData.put("notificationUnit", data.getUnitId());
        caseData.put("ownerUnit", data.getUnitId());

        ModelDAO tbcaseDao = factory.create("tbcase");
        ModelDAOResult resTbcase = tbcaseDao.insert(caseData);

        if (resTbcase.getErrors() != null) {
            throw new EntityValidationException(resTbcase.getErrors());
        }

        return new StandardResult(resTbcase.getId().toString(), null, true);
    }
}
