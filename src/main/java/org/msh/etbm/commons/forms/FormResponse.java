package org.msh.etbm.commons.forms;

import java.util.Map;

/**
 * Created by ricardo on 21/01/16.
 */
public class FormResponse {
    private Map<String, Object> resources;
    private Object doc;

    public Map<String, Object> getResources() {
        return resources;
    }

    public void setResources(Map<String, Object> resources) {
        this.resources = resources;
    }

    public Object getDoc() {
        return doc;
    }

    public void setDoc(Object doc) {
        this.doc = doc;
    }
}
