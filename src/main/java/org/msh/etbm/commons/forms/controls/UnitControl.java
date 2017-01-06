package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.fields.FKUnitField;
import org.msh.etbm.commons.models.data.Field;

/**
 * Created by rmemoria on 25/7/16.
 */
public class UnitControl extends ValuedControl {

    @Override
    protected Field createField() {
        return new FKUnitField();
    }
}
