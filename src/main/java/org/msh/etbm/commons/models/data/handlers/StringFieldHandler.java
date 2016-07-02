package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 1/7/16.
 */
public class StringFieldHandler extends FieldHandler<StringField> {

    @Override
    public Object convertValue(StringField field, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return value;
        }

        return value.toString();
    }

}
