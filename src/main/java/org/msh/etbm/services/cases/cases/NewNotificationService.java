package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Created by Mauricio on 05/09/2016.
 */
@Service
public class NewNotificationService {

    @Autowired
    ModelDAOFactory factory;

    // TODO: registrar commandlog

    @Transactional
    public StandardResult create(CaseFormData data) {
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
