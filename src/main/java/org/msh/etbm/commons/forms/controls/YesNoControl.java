package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.fields.BoolField;
import org.msh.etbm.commons.models.data.Field;

/**
 * Created by rmemoria on 25/7/16.
 */
public class YesNoControl extends ValuedControl {

    @Override
    protected Field createField() {
        return new BoolField();
    }
}
