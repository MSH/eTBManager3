package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.Messages;
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
     * @param doc the document to be validated in JS format
     * @param errors the object to receive the errors
     * @return true if validators were successfully executed
     */
    public static boolean execute(String fieldName, List<Validator> validators,
                                  ScriptObjectMirror jsValidators, SimpleBindings doc, Errors errors,
                                  Messages messages) {
        int index = 0;
        boolean success = true;
        for (Validator validator: validators) {
            JSObject func = (JSObject)jsValidators.get("v" + index);
            boolean res = (boolean)func.call(doc);
            if (!res) {
                String msg = messages != null ? messages.eval(validator.getMessage()) : validator.getMessage();

                if (fieldName != null) {
                    errors.rejectValue(fieldName, null, msg);
                } else {
                    errors.reject(null, msg);
                }
                success = false;
            }
            index++;
        }

        return success;
    }
}
