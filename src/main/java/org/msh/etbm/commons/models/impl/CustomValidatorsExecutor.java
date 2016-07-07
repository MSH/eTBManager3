package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.Validator;
import org.springframework.validation.Errors;

import javax.script.SimpleBindings;
import java.util.List;

/**
 * Created by rmemoria on 6/7/16.
 */
public class CustomValidatorsExecutor {

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
