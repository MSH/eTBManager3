package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.TableColumn;
import org.msh.etbm.commons.models.data.TableColumnType;
import org.msh.etbm.commons.models.data.fields.BoolField;
import org.msh.etbm.commons.models.impl.FieldContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rmemoria on 2/7/16.
 */
public class BoolFieldHandler extends SingleFieldHandler<BoolField> {

    @Override
    protected Object convertValue(BoolField field, FieldContext context, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Boolean) {
            return value;
        }

        if (value instanceof String) {
            return Boolean.parseBoolean((String)value);
        }

        registerConversionError(context);
        return null;
    }

    @Override
    protected void validateValue(BoolField field, FieldContext context, Object value) {

    }

    @Override
    public List<TableColumn> getTableFields(BoolField field) {
        List<TableColumn> lst = new ArrayList<>();

        lst.add(new TableColumn(getFieldName(field), TableColumnType.BOOL));
        return lst;
    }

}
