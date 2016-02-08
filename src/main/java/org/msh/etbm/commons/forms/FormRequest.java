package org.msh.etbm.commons.forms;

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


    public String getStringParam(String name) {
        return params != null ? (String)params.get(name) : null;
    }

    public Integer getIntParam(String name) {
        return params != null ? (Integer)params.get(name) : null;
    }

    public boolean getBoolParam(String name) {
        return params != null ? (Boolean)params.get(name) : false;
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
