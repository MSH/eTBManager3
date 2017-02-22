package org.msh.etbm.commons.forms.impl;

import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rmemoria on 23/7/16.
 */
@Service
public class FormManager {

    @Autowired
    FormStoreService formStoreService;

    @Autowired
    UserRequestService userRequestService;

    public Form get(String formid) {
        Form form = formStoreService.get(formid);
        return form;
    }
}
