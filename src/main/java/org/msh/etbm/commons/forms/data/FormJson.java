package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.models.data.Validator;

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
    private Object title;

    /**
     * The list of default properties
     */
    private Map<String, Object> defaultProperties;

    /**
     * The list of validators of the form
     */
    private List<Validator> validators;


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

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Map<String, Object> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Map<String, Object> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    public void setValidators(List<Validator> validators) {
        this.validators = validators;
    }
}
