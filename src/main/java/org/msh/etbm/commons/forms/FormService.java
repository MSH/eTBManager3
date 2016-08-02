package org.msh.etbm.commons.forms;

import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.impl.FormManager;
import org.msh.etbm.commons.forms.impl.JavaScriptFormGenerator;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public FormInitResponse init(String formId, Object doc) {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();

        Form form = formManager.get(formId);

        FormInitResponse resp = new FormInitResponse();

        String code = javaScriptFormGenerator.generate(form, null);

        resp.setSchema(code);

        return resp;
    }
}
