package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.fields.Field;

/**
 * Created by rmemoria on 25/7/16.
 */
public abstract class ValuedControl extends Control {

    private String property;

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
}
