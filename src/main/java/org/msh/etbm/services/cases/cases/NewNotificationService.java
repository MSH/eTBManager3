package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.entities.EntityValidationException;
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

    @PersistenceContext
    EntityManager entityManager;

    private Map<String, Object> temporaryVar;

    @Transactional
    public void create(CaseFormData data) {
        // temporary until data comes with right format
        mountTempVar(data.getDoc());
        // end of temporary code

        ModelDAO patientDao = factory.create("patient");
        ModelDAOResult resPatient = patientDao.insert((Map)temporaryVar.get("patient"));

        if (resPatient.getErrors() != null) {
            throw new EntityValidationException(resPatient.getErrors());
        }

        ModelDAO tbcaseDao = factory.create("tbcase");
        ModelDAOResult resTbcase = tbcaseDao.insert((Map)temporaryVar.get("tbcase"));

        if (resTbcase.getErrors() != null) {
            throw new EntityValidationException(resTbcase.getErrors());
        }
    }

    /**
     * THIS IS TEMPORARY UNTIL ui SENDS THE REQUEST WITH THE RIGHT FORMAT
     * @param source
     */
    private void mountTempVar(Map<String, Object> source) {
        temporaryVar = new HashMap<>();

        Map<String, Object> patientData = new HashMap<>();
        patientData.put("name", source.get("name"));
        patientData.put("birthDate", source.get("birthDate"));
        patientData.put("motherName", source.get("motherName"));
        patientData.put("gender", source.get("gender"));

        Map<String, Object> caseData = new HashMap<>(source);
        caseData.remove("name");
        caseData.remove("birthDate");
        caseData.remove("motherName");
        caseData.remove("gender");
        caseData.put("patient", null);

        temporaryVar.put("tbcase", caseData);
        temporaryVar.put("patient", patientData);
    }
}
