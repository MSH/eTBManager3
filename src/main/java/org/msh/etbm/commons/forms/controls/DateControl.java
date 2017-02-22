package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.DateField;

/**
 * Created by rmemoria on 25/7/16.
 */
public class DateControl extends ValuedControl {
    @Override
    protected Field createField() {
        return new DateField();
    }
}
