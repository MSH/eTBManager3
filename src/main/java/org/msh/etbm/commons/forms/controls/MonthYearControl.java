package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.MonthYearField;

/**
 * Created by rmemoria on 26/12/16.
 */
public class MonthYearControl extends ValuedControl {


    @Override
    protected Field createField() {
        return new MonthYearField();
    }
}
