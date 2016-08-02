package org.msh.etbm.commons.forms.controls;

import org.msh.etbm.commons.models.data.JSFuncValue;

import java.util.List;

/**
 * Specify the properties of a form control. Basically it contains information on how the control
 * will be rendered in the form, like size, visibility, etc.
 *
 * Created by rmemoria on 23/7/16.
 */
public class Control {
    /**
     * The control type. Each control will have specific features
     */
    private String type;

    private JSFuncValue<Boolean> visible;

    private JSFuncValue<Boolean> readOnly;

    private JSFuncValue<Boolean> disabled;

    private Size size;

    /**
     * If true, the control will start in a new row
     */
    private boolean newRow;

    /**
     * If true, the next control will start in a new row
     */
    private boolean spanRow;

    /**
     * If the control contains other controls, probably this method must be overriden
     * @return
     */
    public List<Control> getControls() {
        return null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSFuncValue<Boolean> getVisible() {
        return visible;
    }

    public void setVisible(JSFuncValue<Boolean> visible) {
        this.visible = visible;
    }

    public JSFuncValue<Boolean> getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(JSFuncValue<Boolean> readOnly) {
        this.readOnly = readOnly;
    }

    public JSFuncValue<Boolean> getDisabled() {
        return disabled;
    }

    public void setDisabled(JSFuncValue<Boolean> disabled) {
        this.disabled = disabled;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public boolean isNewRow() {
        return newRow;
    }

    public void setNewRow(boolean newRow) {
        this.newRow = newRow;
    }

    public boolean isSpanRow() {
        return spanRow;
    }

    public void setSpanRow(boolean spanRow) {
        this.spanRow = spanRow;
    }
}
