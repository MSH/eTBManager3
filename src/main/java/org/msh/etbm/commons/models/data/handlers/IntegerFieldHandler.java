package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.IntegerField;
import org.msh.etbm.commons.models.impl.FieldContext;

/**
 * Created by rmemoria on 1/7/16.
 */
public class IntegerFieldHandler extends SingleFieldHandler<IntegerField> {


    @Override
    protected Object convertValue(IntegerField field, FieldContext fieldContext, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            try {
            return Integer.parseInt((String)value);
            } catch (NumberFormatException e) {
                registerConversionError(fieldContext);
                return value;
            }
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        registerConversionError(fieldContext);
        return value;
    }

    @Override
    protected void validateValue(IntegerField field, FieldContext context, Object value) {

    }

}
