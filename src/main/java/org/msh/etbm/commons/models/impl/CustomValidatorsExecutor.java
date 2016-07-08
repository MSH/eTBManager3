package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.Validator;
import org.springframework.validation.Errors;

import javax.script.SimpleBindings;
import java.util.List;

/**
 * Execute a list of validators. Used both in model custom validators and field custom validators
 * Created by rmemoria on 6/7/16.
 */
public class CustomValidatorsExecutor {

    /**
     * Execute the custom validators
     * @param fieldName the field name (null if in model)
     * @param validators list of {@link Validator} objects
     * @param jsValidators JavaScript object containing the compiled objects
     * @param context the model context, with the document and error messages
     * @return true if validators were successfully executed
     */
    public static boolean execute(String fieldName, List<Validator> validators,
                                  ScriptObjectMirror jsValidators, ModelContext context) {
        SimpleBindings doc = context.getDocBinding();
        Errors errors = context.getErrors();

        int index = 0;
        boolean success = true;
        for (Validator validator: validators) {
            JSObject func = (JSObject)jsValidators.get("v" + index);
            boolean res = (boolean)func.call(doc);
            if (!res) {
                if (fieldName != null) {
                    errors.rejectValue(fieldName, validator.getMessageKey(), validator.getMessage());
                } else {
                    errors.reject(validator.getMessageKey(), validator.getMessage());
                }
                success = false;
            }
            index++;
        }

        return success;
    }
}
