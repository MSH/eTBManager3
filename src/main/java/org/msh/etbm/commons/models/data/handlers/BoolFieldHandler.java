package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.BoolField;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 2/7/16.
 */
public class BoolFieldHandler extends FieldHandler<BoolField> {

    @Override
    protected Object convertValue(BoolField field, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return value;
        }

        if (value instanceof String) {
            return Boolean.parseBoolean((String)value);
        }

        raiseConvertionError();
        return null;
    }

}
