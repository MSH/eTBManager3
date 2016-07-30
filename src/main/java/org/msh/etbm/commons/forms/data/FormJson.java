package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.models.data.JSFuncValue;

import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 24/7/16.
 */
public class FormJson {

    /**
     * The model name. Must be available in the list of models
     */
    private Object model;

    /**
     * The list of controls, in the displaying order
     */
    private List<Map<String, Object>> controls;

    /**
     * The title of the form
     */
    private JSFuncValue<String> title;

    /**
     * The list of default properties
     */
    private Map<String, Object> defaultProperties;


    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public List<Map<String, Object>> getControls() {
        return controls;
    }

    public void setControls(List<Map<String, Object>> controls) {
        this.controls = controls;
    }

    public JSFuncValue<String> getTitle() {
        return title;
    }

    public void setTitle(JSFuncValue<String> title) {
        this.title = title;
    }

    public Map<String, Object> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Map<String, Object> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
}
