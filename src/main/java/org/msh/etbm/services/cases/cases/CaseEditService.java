package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.cases.cases.data.CaseEditFormData;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Mauricio on 03/10/2016.
 */
@Service
public class CaseEditService {

    @Autowired
    FormService formService;

    @Autowired
    ModelDAOFactory factory;

    @PersistenceContext
    EntityManager entityManager;

    public FormInitResponse initForm(UUID id) {
        // mount data
        HashMap<String, Object> data = new HashMap<>();

        ModelDAO tbcaseDao = factory.create("tbcase");
        RecordData resTbcase = tbcaseDao.findOne(id, false);

        ModelDAO patientDao = factory.create("patient");
        RecordData resPatient = patientDao.findOne((UUID)resTbcase.getValues().get("patient"), false);

        data.put("tbcase", resTbcase.getValues());
        data.put("patient", resPatient.getValues());

        // mount form name
        DiagnosisType diag = (DiagnosisType) resTbcase.getValues().get("diagnosisType");
        CaseClassification cla = (CaseClassification) resTbcase.getValues().get("classification");

        // generate form id
        String formid = "case-edit/";
        formid = formid.concat(diag.name().toLowerCase()).concat("-");
        formid = formid.concat(cla.name().toLowerCase());

        return formService.init(formid, data, false);
    }

    @Transactional
    public StandardResult save(CaseEditFormData data) {
        Map<String, Object> patientData = (Map)data.getDoc().get("patient");
        Map<String, Object> caseData = (Map)data.getDoc().get("tbcase");

        // validating and saving patient
        ModelDAO patientDao = factory.create("patient");
        ModelDAOResult resPatient = patientDao.update(data.getPatientId(), patientData);

        if (resPatient.getErrors() != null) {
            throw new EntityValidationException(resPatient.getErrors());
        }

        // prepare case data
        caseData.put("patient", resPatient.getId());

        // validating and saving tbcase
        ModelDAO tbcaseDao = factory.create("tbcase");
        ModelDAOResult resTbcase = tbcaseDao.update(data.getCaseId(), caseData);

        if (resTbcase.getErrors() != null) {
            throw new EntityValidationException(resTbcase.getErrors());
        }

        //TODO: [MSANTOS] improve this archtecture
        //update tbcase age field
        TbCase tbcase = entityManager.find(TbCase.class, resTbcase.getId());
        int updatedAge = tbcase.getUpdatedAge();
        if (updatedAge > 0) {
            tbcase.setAge(updatedAge);
            entityManager.persist(tbcase);
        }

        return new StandardResult(resTbcase.getId().toString(), null, true);
    }
}
