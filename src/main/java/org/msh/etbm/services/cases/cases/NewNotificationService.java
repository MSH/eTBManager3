package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.DiagnosisType;
import org.msh.etbm.services.cases.cases.data.NewNotificationFormData;
import org.msh.etbm.commons.entities.EntityServiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    // TODO: [MSANTOS] registrar commandlog

    public FormInitResponse initForm(CaseClassification cla, DiagnosisType diag) {
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
        String formid = "case-new-notif/";
        formid = formid.concat(diag.name().toLowerCase()).concat("-");
        formid = formid.concat(cla.name().toLowerCase());

        return formService.init(formid, doc, false);
    }

    @Transactional
    public ServiceResult save(NewNotificationFormData data) {

        ModelDAO patientDao = factory.create("patient");
        Map<String, Object> patientData = (Map)data.getDoc().get("patient");

        // validating and saving patient
        ModelDAOResult resPatient;
        resPatient = data.getPatientId() == null ? patientDao.insert(patientData) : patientDao.update(data.getPatientId(), patientData);

        if (resPatient.getErrors() != null) {
            throw new EntityValidationException(resPatient.getErrors());
        }

        // prepare case data
        Map caseData = (Map)data.getDoc().get("tbcase");
        beforeSaveCaseData(caseData, data, resPatient.getId());

        // validating and saving tbcase
        ModelDAO tbcaseDao = factory.create("tbcase");
        ModelDAOResult resTbcase = tbcaseDao.insert(caseData);

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

        ServiceResult res = new ServiceResult();
        res.setOperation(Operation.NEW);
        res.setEntityClass(TbCase.class);
        res.setId(tbcase.getId());

        applicationContext.publishEvent(new EntityServiceEvent(this, res));

        return res;
    }

    /**
     * Prepares case data to be saved setting default parameters
     * @param caseData
     * @param data
     * @param patientId
     */
    private void beforeSaveCaseData (Map<String, Object> caseData, NewNotificationFormData data, UUID patientId) {
        caseData.put("patient", patientId);
        caseData.put("state", CaseState.NOT_ONTREATMENT);
        caseData.put("movedToIndividualized", false);
        caseData.put("validated", false);
        caseData.put("movedSecondLineTreatment", false);
        caseData.put("notificationUnit", data.getUnitId());
        caseData.put("ownerUnit", data.getUnitId());
        caseData.put("currentAddress", caseData.get("notifAddress"));

        if (caseData.get("diagnosisType").equals(DiagnosisType.SUSPECT)) {
            caseData.put("suspectClassification", caseData.get("classification"));
        }
    }
}
