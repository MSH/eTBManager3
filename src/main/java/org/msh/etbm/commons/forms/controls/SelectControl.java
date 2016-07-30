package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.FieldOptions;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.fields.Field;

/**
 * Created by rmemoria on 29/7/16.
 */
public class SelectControl extends ValuedControl {

    private JSFuncValue<FieldOptions> options = new JSFuncValue<>();

    public JSFuncValue<FieldOptions> getOptions() {
        return options;
    }

    public void setOptions(JSFuncValue<FieldOptions> options) {
        this.options = options;
    }

    @Override
    protected Field createField() {
        return null;
    }
}
