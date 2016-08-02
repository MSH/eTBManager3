package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;
import org.springframework.validation.Errors;

import java.util.Map;

/**
 * Execute model and fields validation. This phase is called after the conversion phase
 * (implemented by {@link ModelConverter}
 *
 * Created by rmemoria on 1/7/16.
 */
public class ModelValidator {

    public Errors validate(ValidationContext context, Map<String, Object> vals, ModelResources resources) {
        Model model = context.getModel();

        for (Map.Entry<String, Object> entry: vals.entrySet()) {
            String fname = entry.getKey();
            Field field = model.findFieldByName(fname);
            if (field == null) {
                throw new ModelException("Field not found: " + fname);
            }

            FieldContext fieldCntxt = context.createFieldContext(field);

            Object value = entry.getValue();

            validateField(fieldCntxt, value, resources);
        }

        checkRequiredFields(model, vals, context);

        validateModel(model, context, resources);

        return context.getErrors();
    }


    /**
     * Check non declared fields that are required
     * @param model
     * @param doc
     * @param context
     */
    protected void checkRequiredFields(Model model, Map<String, Object> doc, ValidationContext context) {
        for (Field field: model.getFields()) {
            if (doc.containsKey(field.getName())) {
                continue;
            }

            FieldContext fieldContext = context.createFieldContext(field);
            boolean required = fieldContext.evalBoolProperty("required");
            if (required) {
                context.getErrors().rejectValue(field.getName(), Messages.NOT_NULL);
            }
        }
    }

    protected void validateField(FieldContext fieldContext, Object value, ModelResources resources) {
        FieldHandler handler = FieldTypeManager.instance().getHandler(fieldContext.getField().getTypeName());

        handler.validate(fieldContext.getField(), fieldContext, value, resources);
    }


    protected void validateModel(Model model, ValidationContext context, ModelResources resources) {
        // if there are field validation errors, don't validate the model
        if (context.getErrors().getFieldErrorCount() > 0) {
            return;
        }

        // check if there is any validator to execute
        if (model.getValidators() == null || model.getValidators().size() == 0) {
            return;
        }

        ScriptObjectMirror validators = (ScriptObjectMirror)context.getJsField().get("validators");

        // execute the validators
        CustomValidatorsExecutor.execute(null, model.getValidators(),
                validators,
                context.getDocBinding(),
                context.getErrors(),
                resources.getMessages());
    }

}
