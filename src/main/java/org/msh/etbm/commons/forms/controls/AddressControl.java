package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.AddressField;

/**
 * Control to handle an address (person or unit) in the form
 *
 * Created by rmemoria on 29/8/16.
 */
public class AddressControl extends ValuedControl {

    @Override
    protected Field createField() {
        return new AddressField();
    }
}
