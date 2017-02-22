package org.msh.etbm.commons.models.data.fields;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.commons.models.data.Field;

/**
 * Created by rmemoria on 7/7/16.
 */
public abstract class SingleField extends Field {

    /**
     * Name of the field in the database table
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String fieldName;


    public SingleField() {
        super();
    }

    public SingleField(String name) {
        super(name);
    }

    public SingleField(String name, String fieldName) {
        super(name);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
