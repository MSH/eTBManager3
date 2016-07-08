package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.impl.FieldContext;

/**
 * Created by rmemoria on 1/7/16.
 */
public class StringFieldHandler extends SingleFieldHandler<StringField> {

    @Override
    public Object convertValue(StringField field, FieldContext fieldContext, Object value) {
        if (value == null) {
            return null;
        }

        String s = value instanceof String ? (String)value : value.toString();

        // check charcase
        if (field.getCharCase() != null) {
            switch (field.getCharCase()) {
                case LOWER: s = s.toLowerCase();
                    break;
                case UPPER: s = s.toUpperCase();
                    break;
                default:
            }
        }

        // check extra spaces
        if (field.isTrim()) {
            s = s.trim();
        }

        return s;
    }

    @Override
    protected void validateValue(StringField field, FieldContext context, Object value) {

    }

}
