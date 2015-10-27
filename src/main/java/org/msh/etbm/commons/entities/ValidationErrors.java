package org.msh.etbm.commons.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store list of error messages
 * Created by rmemoria on 27/10/15.
 */
public class ValidationErrors {

    private List<String> globals;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> fields;

    /**
     * Add an error to an specific field
     * @param field
     * @param message
     */
    public void addError(String field, String message) {
        if (fields == null) {
            fields = new HashMap<>();
        }
        fields.put(field, message);
    }

    /**
     * Add a global error
     * @param message
     */
    public void addGlobalError(String message) {
        if (globals == null) {
            globals = new ArrayList<>();
        }
        globals.add(message);
    }

    /**
     * Return the number of error messages
     * @return
     */
    @JsonIgnore
    public int getErrorsCount() {
        return (globals != null? globals.size(): 0) + (fields != null? fields.size(): 0);
    }

    public List<String> getGlobals() {
        return globals;
    }

    public void setGlobals(List<String> globals) {
        this.globals = globals;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}
