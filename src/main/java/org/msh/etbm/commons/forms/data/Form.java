package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.models.data.JSFuncValue;

import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 23/7/16.
 */
public class Form {

    private String model;

    private List<Control> controls;

    private Map<String, JSFuncValue> defaultProperties;


    /**
     * Search for a control by the value being referenced
     * @param value the value reference
     * @return the instance of {@link ValuedControl}, or null if it is not found
     */
    public ValuedControl searchControlByValue(String value) {
        List<Control> controls = getControls();

        if (controls == null) {
            return null;
        }

        for (Control ctrl: controls) {
            if (ctrl instanceof ValuedControl) {
                if (value.equals(((ValuedControl) ctrl).getValue())) {
                    return (ValuedControl)ctrl;
                }
            }
        }

        return null;
    }


    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Map<String, JSFuncValue> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Map<String, JSFuncValue> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
}
