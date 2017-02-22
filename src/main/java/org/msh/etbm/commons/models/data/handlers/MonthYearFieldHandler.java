package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.Tuple;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.FieldHandler;
import org.msh.etbm.commons.models.data.TableColumn;
import org.msh.etbm.commons.models.data.TableColumnType;
import org.msh.etbm.commons.models.data.fields.MonthYearField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Field handle for {@link MonthYearField} fields, i.e, month-year field types
 *
 * Created by rmemoria on 26/12/16.
 */
public class MonthYearFieldHandler extends FieldHandler<MonthYearField> {


    @Override
    protected Object convertValue(MonthYearField field, FieldContext fieldContext, Object value) {
        if (value == null) {
            return null;
        }

        if (!(value instanceof Map)) {
            throw new ModelException("Invalid value for month-year");
        }

        Map<String, Object> map = (Map<String, Object>)value;

        Integer month = asInt(map.get("month"));
        Integer year = asInt(map.get("year"));

        if (month == null && year == null) {
            return null;
        }

        return Tuple.of(month, year);
    }

    /**
     * Convert an object value to an integer type, if possible. If the value is neither a Number
     * nor a String, a {@link ModelException} is thrown
     * @param val the value to be converted
     * @return the int value
     */
    private Integer asInt(Object val) {
        if (val == null) {
            return null;
        }

        if (val instanceof Number) {
            return ((Number)val).intValue();
        }

        if (val instanceof String) {
            return Integer.parseInt((String)val);
        }

        throw new ModelException("Invalid value for month-year: " + val);
    }

    @Override
    protected void validateValue(MonthYearField field, FieldContext context, Object value) {

    }

    @Override
    public Map<String, Object> mapFieldsToSave(MonthYearField field, Object value) {
        return null;
    }

    @Override
    public void dbFieldsToSelect(MonthYearField field, DBFieldsDef defs, boolean displaying) {

    }

    @Override
    public List<TableColumn> getTableFields(MonthYearField field) {
        List<TableColumn> lst = new ArrayList<>();

        lst.add(new TableColumn(field.getFieldMonth(), TableColumnType.INT));
        lst.add(new TableColumn(field.getFieldYear(), TableColumnType.INT));
        return lst;
    }
}
