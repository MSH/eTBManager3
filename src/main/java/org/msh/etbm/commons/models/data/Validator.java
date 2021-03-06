package org.msh.etbm.commons.models.data;

import javax.validation.constraints.NotNull;

/**
 * A validator is responsible for including custom validation in fields or models.
 * It is composed of a Java-Script expression that evaluates the model being validated
 * Created by rmemoria on 1/7/16.
 */
public class Validator {
    /**
     * The java script function expression used in validation process
     */
    @NotNull
    private JSFunction rule;

    /**
     * The message to be displayed. This one is used if messageKey is not defined
     */
    private String message;

    public JSFunction getRule() {
        return rule;
    }

    public void setRule(JSFunction rule) {
        this.rule = rule;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
