package org.msh.etbm.commons.models.data.fields;

/**
 * This is a generic field for supporting of controls that may accept any field type,
 * like the {@link org.msh.etbm.commons.forms.controls.SelectControl}
 *
 * Created by rmemoria on 3/8/16.
 */
@FieldType("any")
public class AnyField extends Field {

    public AnyField() {
    }

    public AnyField(String name) {
        super(name);
    }
}
