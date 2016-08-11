package org.msh.etbm.commons.forms;

import java.util.Map;

/**
 * Response generated by the {@link FormService} with data of form initialization
 *
 * Created by rmemoria on 27/7/16.
 */
public class FormInitResponse {

    /**
     * The document to be edited
     */
    private Map<String, Object> doc;

    /**
     * The resources to initialize the controls
     */
    private Map<String, Object> resources;

    /**
     * The form schema, with list of controls, validators and defaultProperties
     */
    private String schema;

    public Map<String, Object> getDoc() {
        return doc;
    }

    public void setDoc(Map<String, Object> doc) {
        this.doc = doc;
    }

    public Map<String, Object> getResources() {
        return resources;
    }

    public void setResources(Map<String, Object> resources) {
        this.resources = resources;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
