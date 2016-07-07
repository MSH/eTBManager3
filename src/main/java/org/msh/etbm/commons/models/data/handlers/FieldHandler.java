package org.msh.etbm.commons.models.data.handlers;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.FieldType;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.validation.Errors;

import javax.script.SimpleBindings;
import java.util.Map;

/**
 * Created by rmemoria on 1/7/16.
 */
public abstract class FieldHandler<E extends Field> {

    public Class<E> getFieldClass() {
        return ObjectUtils.getGenericType(this.getClass(), 0);
    }


    /**
     * Called during conversion phase, and convert (if necessary) the value to a proper value
     * @param field Instance of {@link Field} representing the field
     * @param fieldContext the context of the field, containing errors and scripts
     * @param value The value to be converted
     * @return the new value
     */
    protected abstract Object convertValue(E field, FieldContext fieldContext, Object value);

    /**
     * Called during the validation phase, to validate the value in the field
     * @param field the field to validate
     * @param context the context of the field, containing errors and scripts
     * @param value the value to be validated
     */
    protected abstract void validateValue(E field, FieldContext context, Object value);

    public String getTypeName() {
        Class clazz = getFieldClass();
        FieldType ftype = (FieldType)clazz.getAnnotation(FieldType.class);
        if (ftype == null) {
            throw new ModelException("Annotation " + FieldType.class.getName() + " not found in class " + clazz.getName());
        }
        
        return ftype.value();
    }

    /**
     * Make any conversion (if necessary) to the field value, or simply return it if no conversion is necessary
     * @param field the instance of {@link Field} representing the field
     * @param value the field value
     * @return the new field value
     */
    public Object convert(E field, FieldContext context, Object value) {
        // check if value is null and there is a default value
        if (value == null && field.getDefaultValue() != null) {
            return field.getDefaultValue();
        }

        return convertValue(field, context, value);
    }


    /**
     * Validate the field
     * @param field
     * @param fieldContext
     * @param value
     */
    public void validate(E field, FieldContext fieldContext, Object value) {
        Errors errors = fieldContext.getParent().getErrors();

        // field value is null ?
        // if is null, then doesn't move to the validation process, but check if it is required
        if (value == null) {
            // is field required ?
            boolean required = fieldContext.evalBoolProperty("required");
            if (required) {
                errors.rejectValue(field.getName(), Messages.NOT_NULL);
            }
            return;
        }

        int errorcount = errors.getErrorCount();

        validateValue(field, fieldContext, value);

        // check if there are new errors
        errorcount -= errors.getErrorCount();

        // run custom validators
        if (errorcount == 0) {
            runCustomValidators(fieldContext);
        }
    }

    private void runCustomValidators(FieldContext fieldContext) {
        Field field = fieldContext.getField();

        if (field.getValidators() == null || field.getValidators().size() == 0) {
            return;
        }

        ScriptObjectMirror validators = fieldContext.getJsField();
        SimpleBindings doc = fieldContext.getParent().getDocBinding();
        Errors errors = fieldContext.getParent().getErrors();

        int index = 0;
        for (Validator validator: field.getValidators()) {
            JSObject func = (JSObject)validators.get("v" + index);
            boolean res = (boolean)func.call(doc);
            if (!res) {
                errors.rejectValue(field.getName(), validator.getMessageKey());
            }
            index++;
        }
    }

    protected void registerConversionError(FieldContext context) {
        context.getParent().getErrors().rejectValue(context.getField().getName(), Messages.NOT_VALID);
    }
}
