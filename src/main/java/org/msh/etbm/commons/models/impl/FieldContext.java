package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.validation.Errors;

/**
 * Define a context for execution of field conversion and validation
 *
 * Created by rmemoria on 5/7/16.
 */
public class FieldContext {

    private ValidationContext context;

    /**
     * The instance of the field containing information about the field schema
     */
    private Field field;

    /**
     * The JavaScript field instance. This object contains JS functions declared in properties of the {@link Field}
     */
    private ScriptObjectMirror jsField;


    public FieldContext(ValidationContext context, Field field, ScriptObjectMirror jsField) {
        this.context = context;
        this.field = field;
        this.jsField = jsField;
    }

    /**
     * Evalue a property that can be a Java Script function or a constant value
     * @param propertyName
     * @return
     */
    public Object evalProperty(String propertyName) {
        Object res = ObjectUtils.getProperty(field, propertyName);

        if (res == null) {
            return false;
        }

        if (!(res instanceof JSFuncValue)) {
            return res;
        }

        JSFuncValue value = (JSFuncValue)res;

        if (value.isValuePresent()) {
            return value.getValue() != null ? value.getValue() : false;
        }

        JSObject func = (JSObject)jsField.get(propertyName);
        return func.call(context.getDoc());
    }

    /**
     * Eval a property that always return a boolean value
     * @param property
     * @return
     */
    public boolean evalBoolProperty(String property) {
        return (boolean)evalProperty(property);
    }

    public Field getField() {
        return field;
    }

    public ScriptObjectMirror getJsField() {
        return jsField;
    }

    public void rejectValue(String group, String defaultMessage) {
        getErrors().rejectValue(getField().getName(), group, defaultMessage);
    }

    public Errors getErrors() {
        return context.getErrors();
    }

    public ValidationContext getContext() {
        return context;
    }
}
