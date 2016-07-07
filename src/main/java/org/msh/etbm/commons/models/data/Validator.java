package org.msh.etbm.commons.models.data;

/**
 * A validator is responsible for including custom validation in fields or models.
 * It is composed of a Java-Script expression that evaluates the model being validated
 * Created by rmemoria on 1/7/16.
 */
public class Validator {
    /**
     * The java script function expression used in validation process
     */
    private String jsExpression;

    /**
     * The message key to be displayed, in case of validation failure
     */
    private String messageKey;

    /**
     * The message to be displayed. This one is used if messageKey is not defined
     */
    private String message;

    public String getJsExpression() {
        return jsExpression;
    }

    public void setJsExpression(String jsExpression) {
        this.jsExpression = jsExpression;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
