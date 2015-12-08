package org.msh.etbm.web.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Standard validation error message sent to the client
 * Created by rmemoria on 19/11/15.
 */
public class Message {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("msg")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;


    public Message() {
        super();
    }

    public Message(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
