package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.options.FieldOptions;
import org.msh.etbm.commons.models.data.options.FormRequestOptions;

import java.util.Map;

/**
 * Created by rmemoria on 29/7/16.
 */
public class SelectControl extends ValuedControl {

    @Override
    protected Field createField() {
        return null;
    }

    @Override
    public FormRequest generateFormRequest(Map<String, Object> doc) {
        Field field = getField();
        if (field == null) {
            return null;
        }

        FieldOptions options = field.getOptions();
        if (options == null) {
            return null;
        }

        if (!(options instanceof FormRequestOptions)) {
            return null;
        }

        FormRequestOptions formReqOptions = (FormRequestOptions)options;

        FormRequest req = new FormRequest();

        req.setId(getId());
        req.setCmd(formReqOptions.getName());

        return req;
    }
}
