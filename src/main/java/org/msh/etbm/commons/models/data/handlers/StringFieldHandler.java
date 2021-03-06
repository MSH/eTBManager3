package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.data.TableColumn;
import org.msh.etbm.commons.models.data.TableColumnType;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.impl.FieldContext;

import java.util.ArrayList;
import java.util.List;

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
        int len = ((String)value).length();

        // check upper boundary
        if (field.getMax() != null && len > field.getMax()) {
            Object[] params = {field.getMax()};
            context.getErrors().rejectValue(field.getName(), Messages.MAX_SIZE, params, null);
        }

        // check lower boundary
        if (field.getMin() != null && len < field.getMin()) {
            Object[] params = {field.getMin()};
            context.getErrors().rejectValue(field.getName(), Messages.MIN_SIZE, params, null);
        }
    }

    @Override
    public List<TableColumn> getTableFields(StringField field) {
        List<TableColumn> fields = new ArrayList<>();

        int size = field.getMax() != null ? field.getMax() : 200;

        fields.add(new TableColumn(getFieldName(field), TableColumnType.VARCHAR, size));
        return fields;
    }

}
