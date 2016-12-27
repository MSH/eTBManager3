package org.msh.etbm.commons.models.data.handlers;

import org.msh.etbm.commons.models.data.fields.YearMonthPeriodField;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.FieldContext;

import java.util.Map;

/**
 * Handle for fields of type period, i.e, holds an initial and final month/year
 * Created by rmemoria on 25/12/16.
 */
public class PeriodFieldHandler extends FieldHandler<YearMonthPeriodField> {

    @Override
    protected Object convertValue(YearMonthPeriodField field, FieldContext fieldContext, Object value) {
        return null;
    }

    @Override
    protected void validateValue(YearMonthPeriodField field, FieldContext context, Object value) {

    }

    @Override
    public Map<String, Object> mapFieldsToSave(YearMonthPeriodField field, Object value) {
        return null;
    }

    @Override
    public void dbFieldsToSelect(YearMonthPeriodField field, DBFieldsDef defs, boolean displaying) {

    }
}
