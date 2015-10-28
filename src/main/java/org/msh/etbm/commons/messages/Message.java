package org.msh.etbm.commons.messages;

/**
 * Created by rmemoria on 27/10/15.
 */
public class Message {
    private String field;
    private String message;
    private String messageGroup;

    public Message() {
        super();
    }

    public Message(String field, String message, String messageGroup) {
        this.field = field;
        this.message = message;
        this.messageGroup = messageGroup;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageGroup() {
        return messageGroup;
    }

    public void setMessageGroup(String messageGroup) {
        this.messageGroup = messageGroup;
    }
}
