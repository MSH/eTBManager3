package org.msh.etbm.commons.forms.impl;

import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.forms.data.DataModel;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.data.MultipleDataModel;
import org.msh.etbm.commons.forms.data.SingleDataModel;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.impl.StandardJSONParser;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 7/8/16.
 */
public class JsonFormParser extends StandardJSONParser<Form> {

    public Form parse(InputStream in) {
        Form frm = parseInputStream(in);

        if (frm.getControls() != null) {
            generateControlsId(frm.getControls(), null);
        }

        return frm;
    }


    /**
     * Automatically generate an ID for the controls if
     * @param controls
     * @param parent
     */
    private void generateControlsId(List<Control> controls, String parent) {
        int index = 0;
        for (Control control: controls) {
            if (control.getId() == null || control.getId().trim().isEmpty()) {
                String ctrlId = "ctrl" + index;

                if (parent != null) {
                    ctrlId = parent + "." + ctrlId;
                }

                control.setId(ctrlId);
            }

            // generate the ID for the child controls
            if (control.getControls() != null) {
                generateControlsId(control.getControls(), control.getId());
            }

            index++;
        }
    }

    @Override
    protected <E> E convertValue(Object value, Class<E> targetClass, Field field) {
        if (value == null) {
            return super.convertValue(value, targetClass, field);
        }

        if (targetClass == DataModel.class) {
            if (value instanceof String) {
                return (E)new SingleDataModel((String)value);
            }

            if (value instanceof Map) {
                return (E)new MultipleDataModel((Map<String, String>)value);
            }

            throw new ModelException("Invalid DataModel value: " + value);
        }

        if (targetClass == Control.class) {
            Map<String, Object> props = (Map<String, Object>)value;
            String typename = (String)props.get("type");
            Class<? extends Control> ctrlClass = ControlFactory.instance().controlClassByTypename(typename);
            if (ctrlClass == null) {
                throw new FormException("Control not found: " + typename);
            }
            return super.convertValue(value, (Class<E>)ctrlClass, field);
        }

        return super.convertValue(value, targetClass, field);
    }

    @Override
    protected Object getBean(Object bean, String propName) {
        if (bean instanceof Control) {
            return getBeanFromControl((Control) bean, propName);
        }
        return super.getBean(bean, propName);
    }

    private Object getBeanFromControl(Control control, String propName) {
        Field field = ObjectUtils.findField(control.getClass(), propName);
        if (field != null) {
            return control;
        }

        if (control instanceof ValuedControl) {
            org.msh.etbm.commons.models.data.Field cf = ((ValuedControl) control).getField();
            if (cf != null && ObjectUtils.findField(cf.getClass(), propName) != null) {
                return cf;
            }
        }
        return null;
    }



}
