package org.msh.etbm.commons.models.data.fields;

import org.msh.etbm.commons.models.data.Field;

/**
 * Created by rmemoria on 26/12/16.
 */
@FieldType("month-year")
public class MonthYearField extends Field {

    private String fieldMonth;
    private String fieldYear;

    public String getFieldMonth() {
        return fieldMonth;
    }

    public void setFieldMonth(String fieldMonth) {
        this.fieldMonth = fieldMonth;
    }

    public String getFieldYear() {
        return fieldYear;
    }

    public void setFieldYear(String fieldYear) {
        this.fieldYear = fieldYear;
    }
}
