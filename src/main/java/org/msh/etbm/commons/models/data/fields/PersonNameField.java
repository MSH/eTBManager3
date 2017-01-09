package org.msh.etbm.commons.models.data.fields;

import org.msh.etbm.commons.models.data.Field;

/**
 * Field structure to handle person's name
 *
 * Created by rmemoria on 26/8/16.
 */
@FieldType("personName")
public class PersonNameField extends Field {

    private String fieldName;
    private String fieldMiddleName;
    private String fieldLastName;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldMiddleName() {
        return fieldMiddleName;
    }

    public void setFieldMiddleName(String fieldMiddleName) {
        this.fieldMiddleName = fieldMiddleName;
    }

    public String getFieldLastName() {
        return fieldLastName;
    }

    public void setFieldLastName(String fieldLastName) {
        this.fieldLastName = fieldLastName;
    }
}
