package org.msh.etbm.services.cases.prevtreats;

import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
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
    public FormInitResponse init(@NotNull UUID id, boolean edit, boolean includeFormLayout) {
        try {
            FormInitResponse res = formService.init("prevtbtreatment", "prevtbtreatment", id, edit);
            if (!includeFormLayout) {
                res.setSchema(null);
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
