package org.msh.etbm.commons.forms;

import java.util.Map;

/**
 * Information about a field sent by the client to be initialized
 * in the server side.
 *
 * Created by rmemoria on 17/1/16.
 */
public class FieldInitRequest {

    /**
     * The field ID, to be sent back in the response
     */
    private String id;

    /**
     * The field type that will be handled by the proper TypeHandler object
     */
    private String type;

    /**
     * The field value
     */
    private Object value;

    /**
     * Extra parameters
     */
    private Map<String, Object> params;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
