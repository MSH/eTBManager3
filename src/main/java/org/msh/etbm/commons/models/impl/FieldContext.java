package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.JSExprValue;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.validation.Errors;

/**
 * Created by rmemoria on 5/7/16.
 */
public class FieldContext {

    private ModelContext parent;

    private Field field;

    private ScriptObjectMirror jsField;

    public FieldContext(ModelContext parent, Field field, ScriptObjectMirror jsField) {
        this.parent = parent;
        this.field = field;
        this.jsField = jsField;
    }

    public boolean evalBoolProperty(String property) {
        Object res = ObjectUtils.getProperty(field, property);

        if (!(res instanceof JSExprValue)) {
            return (boolean)res;
        }

        JSExprValue value = (JSExprValue)res;

        if (value.isValuePresent()) {
            return value.getValue() != null ? (boolean)value.getValue() : false;
        }

        JSObject func = (JSObject)jsField.get(property);
        return (boolean)func.call(parent.getDocBinding());
    }

    public ModelContext getParent() {
        return parent;
    }

    public Field getField() {
        return field;
    }

    public ScriptObjectMirror getJsField() {
        return jsField;
    }
}
