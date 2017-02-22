package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.PersonNameField;

/**
 * Control to display/edit the name of a person
 *
 * Created by rmemoria on 28/8/16.
 */
public class PersonNameControl extends ValuedControl {

    @Override
    protected Field createField() {
        return new PersonNameField();
    }
}
