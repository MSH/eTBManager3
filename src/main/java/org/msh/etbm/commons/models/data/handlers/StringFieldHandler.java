package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 1/7/16.
 */
public class StringFieldHandler extends FieldHandler {

    @Override
    public Object convert(Object value) {
        return null;
    }

    @Override
    public void validate(Object value, Errors errors) {

    }

    @Override
    public Class<? extends Field> getFieldClass() {
        return StringField.class;
    }
}
