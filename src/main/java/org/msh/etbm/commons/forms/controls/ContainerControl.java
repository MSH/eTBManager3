package org.msh.etbm.commons.forms.controls;

import java.util.List;

/**
 * Created by rmemoria on 25/7/16.
 */
public class ContainerControl extends Control {

    private List<Control> controls;

    public List<Control> getControls() {
        return controls;
    }

    public void setControls(List<Control> controls) {
        this.controls = controls;
    }
}
