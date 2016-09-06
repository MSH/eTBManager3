package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.db.PersonName;
import org.msh.etbm.db.entities.Patient;
import org.msh.etbm.db.entities.TbCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @PersistenceContext
    EntityManager entityManager;

    public void create(CaseFormData data){
        ModelDAO patientDao = factory.create("patient");
        ModelDAOResult resPatient = patientDao.insert(getPatientData(data.getDoc()));
        Patient patient = entityManager.find(Patient.class, resPatient.getId());

        ModelDAO tbcaseDao = factory.create("tbcase");
        ModelDAOResult resTbcase = tbcaseDao.insert(getCaseData(data.getDoc(), resPatient.getId()));
        TbCase tbcase = entityManager.find(TbCase.class, resTbcase.getId());

        System.out.println("hey");
    }

    private Map<String, Object> getPatientData(Map<String, Object> source){
        Map<String, Object> patientData = new HashMap<>();

        // mount personName
        Map<String, Object> name = (Map<String, Object>) source.get("name");
        PersonName pName = new PersonName((String)name.get("name"), (String)name.get("middleName"), (String)name.get("lastName"));

        /* mount birthDate
        Date birthDate = null;
        if (source.get("birthDate") != null) {
            birthDate = DateUtils.newDate((String) source.get("birthDate"));
        }
        */

        patientData.put("name", pName);
        /*patientData.put("birthDate", birthDate);*/
        patientData.put("motherName", source.get("motherName"));
        patientData.put("gender", source.get("gender"));

        return patientData;
    }

    private Map<String, Object> getCaseData(Map<String, Object> source, UUID patientId){
        Map<String, Object> caseData = new HashMap<>(source);

        caseData.remove("name");
        caseData.remove("birthDate");
        caseData.remove("motherName");
        caseData.remove("gender");
        caseData.put("patient", patientId);

        return caseData;
    }

}
