package org.msh.etbm.commons.forms.controls;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.models.data.fields.Field;

import java.util.Map;

/**
 * Represents an instance of a {@link Control} that is related to a value in the document
 * being edited
 *
 * Created by rmemoria on 25/7/16.
 */
public abstract class ValuedControl extends Control {

    private String property;

    @JsonIgnore
    private Field field;

    public Field getField() {
        if (field == null) {
            field = createField();
        }
        return field;
    }

    protected abstract Field createField();

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * Generate a form request to return resources to be used by the control
     * @param doc the document of the form
     * @return
     */
    public FormRequest generateFormRequest(Map<String, Object> doc) {
        return null;
    }

    public Object gerValue(Map<String, Object> doc) {
        if (property == null) {
            return null;
        }

        if (property.contains(".")) {
            throw new FormException("nested property not implemented yet: " + property);
        }

        return doc.get(property);
    }
}
