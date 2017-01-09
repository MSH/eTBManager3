package org.msh.etbm.services.cases.prevtreats;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 6/12/16.
 */
@Service
public class PrevTBTreatmentService {


    @Autowired
    ModelDAOFactory modelDAOFactory;

    @Autowired
    FormService formService;

    @Transactional
    public FormInitResponse initNew(@NotNull UUID caseId) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("caseId", caseId);
        return formService.init("prevtbtreatment.edit", doc, false);
    }

    @Transactional
    public FormInitResponse initEdit(@NotNull UUID id, boolean displaying) {
        FormInitResponse res = formService.init("prevtbtreatment", id, displaying);
        return res;
    }

}
