package org.msh.etbm.commons.models.data.fields;

/**
 * Created by rmemoria on 2/7/16.
 */
@FieldType("date")
public class DateField extends SingleField {
    public DateField() {
        super();
    }

    public DateField(String name) {
        super(name);
    }

    public DateField(String name, String dbFieldName) {
        super(name, dbFieldName);
    }
}
