package org.msh.etbm.commons.forms.impl;

import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by rmemoria on 23/7/16.
 */
@Service
public class FormManager {

    @Autowired
    FormStoreService formStoreService;

    @Autowired
    UserRequestService userRequestService;

    public Form get(String formid, UUID workspaceId) {
        String resourcePath = "/forms/" + formid + ".json";
        Form frm = null;

        try {
            ClassPathResource res = new ClassPathResource(resourcePath);

            FormParser p = new FormParser();
            frm = p.parse(res.getInputStream());
        } catch (IOException e) {
            throw new FormException("Error when trying to load resource " + resourcePath);
        }

        return frm;
    }
}
