package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.models.data.JSFuncValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a form, containing controls, default values and validators to display, handle and validate
 * forms in server and client side
 *
 * Created by rmemoria on 23/7/16.
 */
public class Form {

    private DataModel dataModel;

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

    /**
     * Return a list of all instances of {@link ValuedControl} found in the form, inclusive
     * child controls inside containers
     *
     * @return
     */
    public List<ValuedControl> collectAllValuedControls() {
        List<ValuedControl> lst = new ArrayList<>();

        recursiveControlSearch(getControls(), lst);

        return lst;
    }


    /**
     * Browse the control tree and retrieve all controls of type {@link ValuedControl} inside
     * the given list
     * @param controls
     * @param list
     */
    private void recursiveControlSearch(List<Control> controls, List<ValuedControl> list) {
        for (Control control: controls) {
            if (control instanceof ValuedControl) {
                ValuedControl vc = (ValuedControl)control;
                list.add(vc);
            }

            if (control.getControls() != null) {
                recursiveControlSearch(control.getControls(), list);
            }
        }
    }

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }

    public DataModel getDataModel() {
        return dataModel;
    }

    public void setDataModel(DataModel dataModel) {
        this.dataModel = dataModel;
    }

    public Map<String, JSFuncValue> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Map<String, JSFuncValue> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
}
