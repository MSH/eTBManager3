package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.fields.BoolField;
import org.msh.etbm.commons.models.data.fields.Field;

/**
 * Created by rmemoria on 26/12/16.
 */
public class BoolControl extends ValuedControl {

    @Override
    protected Field createField() {
        return new BoolField();
    }
}
