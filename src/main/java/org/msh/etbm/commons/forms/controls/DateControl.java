package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.fields.DateField;
import org.msh.etbm.commons.models.data.fields.Field;

/**
 * Created by rmemoria on 25/7/16.
 */
public class DateControl extends ValuedControl {
    @Override
    protected Field createField() {
        return new DateField();
    }
}
