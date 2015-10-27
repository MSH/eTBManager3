package org.msh.etbm.web.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * Standard result structure for API requests. Not a rule, but used by many API functions to indicate the
 * result of a request
 *
 * Created by rmemoria on 27/10/15.
 */
public class StandardResult {
    /**
     * Indicate if the request was succeeded (true) or failed (false)
     */
    private boolean success;

    /**
     * The specific result to be sent back (serialized) to the client
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object result;

    /**
     * The list of error messages, in case validation fails
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;


    public StandardResult() {
        super();
    }

    public StandardResult(Object res, Map<String, String> errors) {
        this.result = res;
        this.errors = errors;
        success = errors == null || errors.size() == 0;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
