package org.msh.etbm.commons.forms.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.data.FormJson;
import org.msh.etbm.commons.forms.data.MultipleDataModel;
import org.msh.etbm.commons.forms.data.SingleDataModel;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parse a form from a JSON representation to a {@link Form} object
 *
 * Created by rmemoria on 25/7/16.
 */
public class FormParser {

    /**
     * Single method that parse the json from the given input stream object
     * @param in the instance of InputStream, source of the form schema in json format
     * @return the instance of {@link Form} containing the parsed json data
     */
    public Form parse(InputStream in) {
        FormJson frm;
        try {
            ObjectMapper mapper = new ObjectMapper();
            frm = mapper.readValue(in, FormJson.class);
        } catch (Exception e) {
            throw new FormException(e);
        }

        Form res = new Form();

        if (frm.getModel() instanceof Map) {
            Map<String, String> variables = (Map<String, String>)frm.getModel();
            res.setDataModel(new MultipleDataModel(variables));
        } else if (frm.getModel() instanceof String) {
            res.setDataModel(new SingleDataModel((String)frm.getModel()));
        }

        List<Control> controls = importControlList(frm.getControls());
        res.setControls(controls);

        Map<String, JSFuncValue> defaultProperties = importDefaultProperties(frm.getDefaultProperties());
        res.setDefaultProperties(defaultProperties);

        res.setTitle(convertToJSFuncValue(frm.getTitle(), String.class));

        res.setValidators(frm.getValidators());

        generateControlsId(res.getControls(), null);

        return res;
    }

    private Map<String, JSFuncValue> importDefaultProperties(Map<String, Object> props) {
        Map<String, JSFuncValue> defProps = new HashMap<>();

        for (Map.Entry<String, Object> entry: props.entrySet()) {
            JSFuncValue val = convertToJSFuncValue(entry.getValue(), Object.class);
            defProps.put(entry.getKey(), val);
        }
        return defProps;
    }


    private List<Control> importControlList(List<Map<String, Object>> controls) {
        if (controls == null) {
            return null;
        }

        List<Control> ctrls = new ArrayList<>();

        for (Map<String, Object> ctrlProps: controls) {
            Control ctrl = importControl(ctrlProps);
            ctrls.add(ctrl);
        }

        return ctrls;
    }

    /**
     * Import values from a map to the control
     * @param props
     * @return
     */
    private Control importControl(Map<String, Object> props) {
        String type = (String)props.get("type");

        if (type == null) {
            throw new FormException("No type defined for control in JSON data");
        }

        Control control = ControlFactory.instance().createByTypename(type);

        for (Map.Entry<String, Object> entry: props.entrySet()) {
            if ("type".equals(entry.getKey())) {
                continue;
            }

            Object value = entry.getValue();

            if ("controls".equals(entry.getKey())) {
                value = importControlList((List<Map<String, Object>>)entry.getValue());
            }

            writeProperty(control, entry.getKey(), value);
        }

        return control;
    }

    /**
     * Write the property from a map value to the bean. The value is converted according
     * to the bean property type. If the bean is an instance of {@link ValuedControl} and
     * the property is not found, so the field property of the instance of {@link ValuedControl}
     * is used to check if property exists
     * @param control
     * @param prop
     * @param value
     */
    private void writeProperty(Object control, String prop, Object value) {
        try {
            Object bean = control;
            Class propType = PropertyUtils.getPropertyType(bean, prop);
            if (propType == null && (control instanceof ValuedControl)) {
                bean = ((ValuedControl) control).getField();
                if (bean != null) {
                    propType = PropertyUtils.getPropertyType(bean, prop);
                }
            }

            if (propType == null) {
                throw new FormException("Property not found: " + prop + " in " + bean.getClass());
            }

            Object propValue = getValueToWrite(bean, prop, value, propType);

            PropertyUtils.setProperty(bean, prop, propValue);

        } catch (Exception e) {
            throw new FormException(e);
        }
    }

    private Object getValueToWrite(Object bean, String propName, Object value, Class propType) throws Exception {
        if (value == null) {
            return null;
        }

        // check if property is an instance of JSFuncValue
        if (propType == JSFuncValue.class) {
            Class targetType = ObjectUtils.getPropertyGenericType(bean.getClass(), propName, 0);
            return convertToJSFuncValue(value, targetType);
        }

        // check if it is a map representing an object
        if (value instanceof Map && !Map.class.isAssignableFrom(propType)) {
            return convertFromMap((Map<String, Object>)value, propType);
        }

        return value;
    }

    private Object convertFromMap(Map<String, Object> props, Class destType) {
        Object bean = ObjectUtils.newInstance(destType);

        for (Map.Entry<String, Object> entry: props.entrySet()) {
            writeProperty(bean, entry.getKey(), entry.getValue());
        }
        return bean;
    }

    private JSFuncValue convertToJSFuncValue(Object value, Class targetType) {
        if (value == null) {
            return null;
        }

        if (value instanceof Map) {
            Map map = (Map)value;
            if (map.size() == 1) {
                if (map.containsKey("function")) {
                    return JSFuncValue.function((String)map.get("function"));
                }

                if (map.containsKey("value")) {
                    return JSFuncValue.of(map.get("value"));
                }
            }
        }

        if (targetType == null || targetType.isAssignableFrom(value.getClass())) {
            return JSFuncValue.of(value);
        }

        throw new FormException("Invalid property " + value);
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
}
