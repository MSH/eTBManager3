package org.msh.etbm.commons.models.data.handlers;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.FieldType;
import org.msh.etbm.commons.models.db.DBFieldsDef;
import org.msh.etbm.commons.models.impl.CustomValidatorsExecutor;
import org.msh.etbm.commons.models.impl.FieldContext;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.validation.Errors;

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

        if (!validateOptions(fieldContext, value)) {
            return;
        }

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

    /**
     * Check if field value is one of the valid options defined for the field
     * @param fieldContext
     * @param value
     * @return
     */
    private boolean validateOptions(FieldContext fieldContext, Object value) {
        // check if there is any option available
        Field field = fieldContext.getField();
        if (field.getOptions() == null) {
            return true;
        }

        boolean success = field.getOptions().isValueInOptions(value);

        if (!success) {
            fieldContext.getParent().getErrors().rejectValue(field.getName(), Messages.NOT_VALID_OPTION);
        }

        return success;
    }

    /**
     * Execute any custom validator defined in the field
     * @param fieldContext
     */
    private void runCustomValidators(FieldContext fieldContext) {
        Field field = fieldContext.getField();

        if (field.getValidators() == null || field.getValidators().size() == 0) {
            return;
        }

        ScriptObjectMirror validators = (ScriptObjectMirror)fieldContext.getJsField().get("validators");

        CustomValidatorsExecutor.execute(field.getName(), field.getValidators(), validators, fieldContext.getParent());
    }

    protected void registerConversionError(FieldContext context) {
        context.getParent().getErrors().rejectValue(context.getField().getName(), Messages.NOT_VALID);
    }

    /**
     * Define the fields to be saved
     */
    public abstract Map<String, Object> mapFieldsToSave(E field, Object value);

    /**
     * Add the fields to be selected in order to recover the information from the database
     * @param defs the implementation of {@link DBFieldsDef} that will receive the fields
     * @param displaying indicate if the return operation will be used for displaying data. In this
     *                   case, if a table join or a code, it must include data to be displayed
     */
    public abstract void dbFieldsToSelect(E field, DBFieldsDef defs, boolean displaying);

    /**
     * Called when value is read from the database. This method is called if the method
     * {@link FieldHandler#dbFieldsToSelect(E, DBFieldsDef, boolean)} define one single field to select
     * from the database table.
     * This is the moment to make any necessary conversion
     * of the value read from DB.
     * @param value the value read from the database
     * @return the value to be sent to the model object
     */
    public Object readSingleValueFromDb(E field, Object value) {
        return value;
    }

    /**
     * This method must be override in order to map the values from the DB to a single value to the
     * model object.
     * <p/>
     * Called when multiple values are read from the database. This method is called if the return of
     * {@link FieldHandler#dbFieldsToSelect(E, DBFieldsDef, boolean)} define more than one field to select
     * from the database table.
     * <p/>
     * This is the moment to perform any conversion of the value read from the DB and return a single
     * value to the model object.
     * @param values a map containing DB field name and its value
     * @return the object that will be set in the model object
     */
    public Object readMultipleValuesFromDb(E field, Map<String, Object> values) {
        throw new ModelException("this method must be implemented");
    }
}
