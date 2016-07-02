package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.IntegerField;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 1/7/16.
 */
public class IntegerFieldHandler extends FieldHandler<IntegerField> {


    @Override
    protected Object convertValue(IntegerField field, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return Integer.parseInt((String)value);
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        throw new ModelException("Invalid type for convertion");
    }
}
