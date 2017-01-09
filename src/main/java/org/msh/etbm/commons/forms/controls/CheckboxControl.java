package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.BoolField;

/**
 * Created by rmemoria on 25/7/16.
 */
public class CheckboxControl extends ValuedControl {

    @Override
    protected Field createField() {
        return new BoolField();
    }
}
