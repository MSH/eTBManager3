package org.msh.etbm.commons.forms.impl;

import org.msh.etbm.commons.forms.data.Form;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmemoria on 23/7/16.
 */
public class FormManager {

    private Map<String, Form> forms = new HashMap<>();

    public Form get(String formid) {
        Form frm = forms.get(formid);

        if (frm == null) {
            return null;
        }

        return frm;
    }
}
