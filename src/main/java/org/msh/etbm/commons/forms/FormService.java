package org.msh.etbm.commons.forms;

import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.impl.FormManager;
import org.msh.etbm.commons.forms.impl.JavaScriptFormGenerator;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    FormManager formManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    JavaScriptFormGenerator javaScriptFormGenerator;

    @Autowired
    FormRequestService formRequestService;


    /**
     * Generate an initialization response to be sent to the client containing the form schema,
     * the document to be applied to the form and the resources for the form controls
     * @param formId the unique form ID
     * @param doc the document
     * @return
     */
    public FormInitResponse init(String formId, Map<String, Object> doc) {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();

        Form form = formManager.get(formId);

        FormInitResponse resp = new FormInitResponse();

        String code = javaScriptFormGenerator.generate(form, null);

        resp.setSchema(code);
        resp.setDoc(doc);

        return resp;
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
