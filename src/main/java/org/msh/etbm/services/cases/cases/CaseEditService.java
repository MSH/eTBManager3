package org.msh.etbm.services.cases.cases;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.DiagnosisType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        String formid = "case-update/";
        formid = formid.concat(diag.name().toLowerCase()).concat("-");
        formid = formid.concat(cla.name().toLowerCase());

        return formService.init(formid, data, false);
    }
}
