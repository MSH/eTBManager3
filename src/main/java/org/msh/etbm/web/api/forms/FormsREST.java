package org.msh.etbm.web.api.forms;

import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.impl.FormManager;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller exposing APIs for form handling
 *
 * Created by rmemoria on 8/1/17.
 */
@RestController
@RequestMapping("/api/forms")
@Authenticated
public class FormsREST {

    @Autowired
    FormManager formManager;

    @RequestMapping(value = "/{formId}", method = RequestMethod.GET)
    public Form getForm(@PathVariable String formId) {
        Form form = formManager.get(formId);
        return form;
    }
}
