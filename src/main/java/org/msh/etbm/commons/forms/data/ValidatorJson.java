package org.msh.etbm.commons.forms.data;

/**
 * Created by rmemoria on 31/7/16.
 */
public class ValidatorJson {

    private String rule;
    private String message;
    private String messageKey;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
