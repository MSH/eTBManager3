package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.IntegerField;

/**
 * Created by rmemoria on 25/7/16.
 */
public class IntegerControl extends ValuedControl {
    @Override
    protected Field createField() {
        return new IntegerField();
    }
}
