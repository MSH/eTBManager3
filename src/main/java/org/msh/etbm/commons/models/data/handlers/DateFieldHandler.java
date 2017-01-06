package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.TableColumn;
import org.msh.etbm.commons.models.data.TableColumnType;
import org.msh.etbm.commons.models.data.fields.DateField;
import org.msh.etbm.commons.models.impl.FieldContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rmemoria on 2/7/16.
 */
public class DateFieldHandler extends SingleFieldHandler<DateField> {

    @Override
    protected Object convertValue(DateField field, FieldContext context, Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Date) {
            return value;
        }

        if (value instanceof String) {
            try {
                String dateStr = (String) value;
                SimpleDateFormat sdf;

                if (dateStr.length() == 10) {
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                } else {
                    sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
                }

                Date date = sdf.parse(dateStr);
                return date;
            } catch (ParseException e) {
                registerConversionError(context);
                return null;
            }
        }

        registerConversionError(context);
        return value;
    }

    @Override
    protected void validateValue(DateField field, FieldContext context, Object value) {

    }

    @Override
    public List<TableColumn> getTableFields(DateField field) {
        List<TableColumn> lst = new ArrayList<>();

        lst.add(new TableColumn(getFieldName(field), TableColumnType.DATE));
        return lst;
    }

}
