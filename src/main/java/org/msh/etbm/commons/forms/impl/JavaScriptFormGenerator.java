package org.msh.etbm.commons.forms.impl;

import org.apache.commons.beanutils.PropertyUtils;
import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.models.data.JSFuncValue;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 26/7/16.
 */
public class JavaScriptFormGenerator {

    public String generate(Form form, String funcName) {
        StringBuilder s = new StringBuilder();

        s.append("function ").append(funcName).append("() { return ");

        createJSObject(form, s);

        s.append(";\n}");

        return s.toString();
    }

    private void createJSObject(Form frm, StringBuilder s) {
        s.append("{\n");

        String delim = "";
        if (frm.getDefaultProperties() != null) {
            addDefaultProperties(frm, s);
            delim = ",\n";
        }

        if (frm.getControls() != null) {
            s.append(delim);
            createControlsScript(frm.getControls(), s);
            delim = ",\n";
        }
        s.append("\n}");
    }

    private void createControlsScript(List<Control> controls, StringBuilder s) {
        s.append("controls:[");
        String delim = "";
        for (Control control: controls) {
            s.append(delim);
            s.append(generateControlScript(control));
            delim = ",";
        }
        s.append("\n]");
    }

    private String generateControlScript(Control control) {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        try {
            Map<String, Object> props = PropertyUtils.describe(control);

            // collect the properties to generate
            Object field = props.get("field");

            if (field != null) {
                Map<String, Object> extra = PropertyUtils.describe(control);
                props.putAll(extra);
            }
            props.remove("field");
            props.remove("class");

            // mount the list of properties
            String delim = "";
            for (Map.Entry<String, Object> entry: props.entrySet()) {
                if (entry.getValue() != null) {
                    String sval = convertValue(entry.getValue());
                    if (sval != null) {
                        s.append(delim).append(entry.getKey()).append(": ").append(sval);
                        delim = ",\n";
                    }
                }
            }

        } catch (Exception e) {
            throw new FormException(e);
        }
        s.append("\n}");

        return s.toString();
    }

    private void addDefaultProperties(Form frm, StringBuilder s) {
        s.append("defaultProperties:{");

        String delim = "\n";
        for (Map.Entry<String, JSFuncValue> entry: frm.getDefaultProperties().entrySet()) {
            s.append(delim).append(entry.getKey()).append(':');

            JSFuncValue val = entry.getValue();
            if (val.isExpressionPresent()) {
                s.append("function() { return ").append(val.getFunction()).append("; }");
            } else {
                s.append(convertValue(val.getValue()));
            }
            delim = ",\n";
        }
        s.append("\n}");
    }

    private String convertValue(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            return "'" + value + "'";
        }

        if (value instanceof Boolean) {
            return (Boolean)value ? "true" : "false";
        }

        if (value instanceof Number) {
            return value.toString();
        }

        if (value instanceof Date) {
            Date dt = (Date)value;
            return "new Date(" + dt.getTime() + ")";
        }

        if (value instanceof JSFuncValue) {
            JSFuncValue f = (JSFuncValue)value;
            if (f.isExpressionPresent()) {
                return "function() { return " + f.getFunction() + "; }";
            }
            return convertValue(f.getValue());
        }

        if (value instanceof Control) {
            return generateControlScript((Control)value);
        }

        if (value instanceof List) {
            List lst = (List)value;
            return convertList(lst);
        }

        return convertObject(value);
    }


    private String convertList(List lst) {
        StringBuilder s = new StringBuilder();
        s.append("[");

        String delim = "";
        for (Object item: lst) {
            String sitem = convertValue(item);
            s.append(delim).append(sitem);
            delim = ", ";
        }

        s.append("]");

        return s.toString();
    }

    private String convertObject(Object obj) {
        StringBuilder s = new StringBuilder();

        s.append("{ ");
        try {
            Map<String, Object> props = PropertyUtils.describe(obj);
            props.remove("class");

            for (Map.Entry<String, Object> prop: props.entrySet()) {
                String delim = "";
                String sval = convertValue(prop.getValue());
                if (sval != null) {
                    s.append(delim).append(prop.getKey()).append(": ").append(sval);
                    delim = ", ";
                }
            }
        } catch (Exception e) {
            throw new FormException(e);
        }
        s.append(" }");

        return s.toString();
    }
}
