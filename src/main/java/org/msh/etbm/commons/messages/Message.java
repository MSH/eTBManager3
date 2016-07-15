package org.msh.etbm.commons.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a message related to a field. Usually these messages are related to validation
 * errors, but may be used in other contexts
 * <p>
 * Created by rmemoria on 27/10/15.
 */
public class Message {
    /**
     * The name of the field related to the message. Null indicates it is a global message
     */
    private String field;

    /**
     * The message
     */
    @JsonProperty("msg")
    private String message;

    /**
     * The group of the message. For automation purposes, like tests for example.
     * The group represents the nature of the message, regardless of the language
     */
    @JsonProperty("group")
    private String messageGroup;

    /**
     * Default constructor
     */
    public Message() {
        super();
    }

    /**
     * Constructor to fill the object properties
     *
     * @param field
     * @param message
     * @param messageGroup
     */
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
