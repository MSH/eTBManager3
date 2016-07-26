package org.msh.etbm.commons.forms.impl;

import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.controls.*;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Standard control factory for creation of form control
 * Created by rmemoria on 25/7/16.
 */
public class ControlFactory {

    private static final ControlFactory _instance = new ControlFactory();

    private Map<String, Class<? extends Control>> controls = new HashMap<>();

    protected ControlFactory() {
        register("string", StringControl.class);
        register("int", IntegerControl.class);
        register("yesNo", YesNoControl.class);
        register("checkbox", CheckboxControl.class);
        register("date", DateControl.class);
        register("text", TextControl.class);
        register("container", ContainerControl.class);
        register("subtitle", SubtitleControl.class);
    }

    /**
     * Register a control
     * @param typename
     * @param controlClass
     */
    public void register(String typename, Class<? extends Control> controlClass) {
        controls.put(typename, controlClass);
    }

    public Control createByTypename(String name) {
        Class<? extends Control> clazz = controls.get(name);

        if (clazz == null) {
            throw new FormException("No control found registered with name " + name);
        }

        Control ctrl = ObjectUtils.newInstance(clazz);

        return ctrl;
    }

    public static final ControlFactory instance() {
        return _instance;
    }
}
