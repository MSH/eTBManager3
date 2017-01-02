package org.msh.etbm.services.cases.followup.examculture;

import org.apache.commons.collections.map.HashedMap;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.db.RecordData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 5/12/16.
 */
@Service
public class ExamCulture2Service {

    @Autowired
    ModelDAOFactory modelDAOFactory;

    @Autowired
    FormService formService;

    /**
     * Return the data from an culture exam and its form layout
     * @param id the ID of the culture exam. If null, it is considered a new culture exam
     * @param edit if true, the data will be used in an edit form
     * @param includeFormLayout if true, the result will include form layout
     * @return instance of {@link FormInitResponse}
     */
    @Transactional
    public FormInitResponse init(@NotNull UUID id, boolean edit, boolean includeFormLayout) {
        // initialize the data to be sent to the client
        Map<String, Object> doc;
        if (id == null) {
            ModelDAO dao = modelDAOFactory.create("examculture");
            RecordData data = dao.findOne(id, !edit);
            doc = data.getValues();
        } else {
            doc = new HashedMap();
        }

        return formService.init("examculture", doc, !edit);
    }
}
