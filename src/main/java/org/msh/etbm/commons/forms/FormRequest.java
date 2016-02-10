package org.msh.etbm.commons.forms;

import org.msh.etbm.commons.InvalidArgumentException;

import java.util.Map;

/**
 * Form request data to be sent by the client
 * Created by ricardo on 21/01/16.
 */
public class FormRequest {
    /**
     * The command to handle
     */
    private String cmd;

    /**
     * The request ID, returned in the service response
     */
    private String id;

    /**
     * The request parameters
     */
    private Map<String, Object> params;


    /**
     * Return a parameter value as string type. If parameter value is not of string type,
     * an {@link InvalidArgumentException} is thrown
     * @param name the parameter name
     * @return String value, or null if value is not found
     */
    public String getStringParam(String name) {
        if (params == null) {
            return null;
        }

        Object value = params.get(name);
        if (value == null) {
            return null;
        }

        if (!(value instanceof String)) {
            throw new InvalidArgumentException(name, "Invalid type. Expected string", null);
        }
        return (String)value;
    }

    /**
     * Return a parameter value as an integer type. If parameter value is not of number type,
     * an {@link InvalidArgumentException} is thrown
     * @param name the parameter name
     * @return an Integer value, or null if parameter value is not found
     */
    public Integer getIntParam(String name) {
        if (params == null) {
            return null;
        }

        Object value = params.get(name);
        if (value == null) {
            return null;
        }

        if (!(value instanceof Number)) {
            throw new InvalidArgumentException(name, "Invalid type. Expected number", null);
        }
        return ((Number)value).intValue();
    }

    /**
     * Return a parameter value as a boolean type. If parameter value is not of boolean type,
     * an {@link InvalidArgumentException} is thrown
     * @param name the parameter name
     * @return a boolean value, or null if parameter value is not found
     */
    public Boolean getBoolParam(String name) {
        if (params == null) {
            return null;
        }

        Object value = params.get(name);
        if (value == null) {
            return null;
        }

        if (!(value instanceof Boolean)) {
            throw new InvalidArgumentException(name, "Invalid type. Expected boolean", null);
        }
        return (Boolean)value;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
