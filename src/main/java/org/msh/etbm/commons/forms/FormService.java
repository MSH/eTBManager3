package org.msh.etbm.commons.forms;

import org.apache.commons.collections.map.HashedMap;
import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.impl.FormManager;
import org.msh.etbm.commons.forms.impl.FormStoreService;
import org.msh.etbm.commons.forms.impl.JavaScriptFormGenerator;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Form services used for initialization and submission of data
 *
 * Created by rmemoria on 27/7/16.
 */
@Service
public class FormService {

    @Autowired
    FormStoreService formStoreService;

    @Autowired
    JavaScriptFormGenerator javaScriptFormGenerator;

    @Autowired
    FormRequestService formRequestService;

    @Autowired
    ModelDAOFactory modelDAOFactory;


    /**
     * Generate an initialization response to be sent to the client containing the form schema,
     * the document to be applied to the form and the resources for the form controls
     * @param formId the unique form ID
     * @param doc the document
     * @param displaying if true, form will be prepared for displaying, otherwise, for editing
     * @return
     */
    public FormInitResponse init(String formId, Object doc, boolean displaying) {
        Map<String, Object> propValues = doc instanceof Map ? (Map<String, Object>)doc : ObjectUtils.describeProperties(doc);

        Form form = formStoreService.get(formId);

        FormInitResponse resp = new FormInitResponse();

        String code = javaScriptFormGenerator.generate(form, null);

        resp.setSchema(code);
        resp.setDoc(propValues);

        if (!displaying) {
            Map<String, Object> resources = generateResources(form, propValues);
            resp.setResources(resources);
        }

        return resp;
    }


    public FormInitResponse initFromModel(@NotNull String modelId, UUID id, boolean displaying) {
        // initialize the data to be sent to the client
        Map<String, Object> doc;
        if (id != null) {
            ModelDAO dao = modelDAOFactory.create(modelId);
            RecordData data = dao.findOne(id, displaying);
            doc = data.getValues();
        } else {
            doc = new HashedMap();
        }

        String formId = modelId + (displaying ? ".ro" : ".edit");

        return init(formId, doc, displaying);
    }

    /**
     * Generate the control resources to be sent to the client. The resources will be serialized to JSON
     * and sent back to the client
     *
     * @param form the form containing the resources
     * @param doc the document with the control values
     * @return the map containing control ID and its resource
     */
    protected Map<String, Object> generateResources(Form form, Map<String, Object> doc) {
        List<FormRequest> reqs = generateFormRequests(form, doc);
        return formRequestService.processFormRequests(reqs);
    }

    /**
     * Generate the control requests to be processed in order to create the client control resources
     * @param form
     * @param doc
     * @return
     */
    protected List<FormRequest> generateFormRequests(Form form, Map<String, Object> doc) {
        List<FormRequest> reqs = new ArrayList<>();

        List<ValuedControl> controls = form.collectAllValuedControls();
        for (ValuedControl ctrl: controls) {

            FormRequest req = ctrl.generateFormRequest(doc);
            if (req == null) {
                continue;
            }

            reqs.add(req);
        }

        return reqs;
    }
}
