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
 * Generate java script code to be sent to the client (browser) with a form schema.
 * The code generated is wrapped by a function and can be executed with an eval function
 * in the javascript environment.
 *
 * Created by rmemoria on 26/7/16.
 */
public class JavaScriptFormGenerator {

    /**
     * Private constructor to avoid initialization of this class
     */
    private JavaScriptFormGenerator() {
        super();
    }

    /**
     * Generate the script for the give form exposing the function as the given function name
     * @param form the instance of {@link Form}
     * @param funcName the name of the java script function to be declared in the script. If a null
     *                 value is given, the script will generate the object code
     * @return The java script code
     */
    public static final String generate(Form form, String funcName) {
        JavaScriptFormGenerator instance = new JavaScriptFormGenerator();
        return instance.generateScript(form, funcName);
    }


    /**
     * Generate the java script code with a form schema to be executed by the browser
     * @param form the form data, instance of {@link Form}
     * @param funcName the name of the function to be declared as the entry point function in the script
     * @return the java script code
     */
    private String generateScript(Form form, String funcName) {
        StringBuilder s = new StringBuilder();

        if (funcName != null) {
            s.append("function ").append(funcName).append("() { return ");
        }

        generateFormObjectScript(form, s);

        if (funcName != null) {
            s.append(";\n}");
        }

        return s.toString();
    }

    /**
     * Generate the form to a java script object code
     * @param frm object with information about the form
     * @param s the instance of StringBuilder to receive the script code
     */
    private void generateFormObjectScript(Form frm, StringBuilder s) {
        s.append("{\n");

        String delim = "";
        if (frm.getDefaultProperties() != null) {
            generateDefaultPropertiesCode(frm, s);
            delim = ",\n";
        }

        if (frm.getControls() != null) {
            s.append(delim);
            createControlsScript(frm.getControls(), s);
            delim = ",\n";
        }
        s.append("\n}");
    }

    /**
     * Generate a java script code for the list of controls in the form
     * @param controls the list of controls
     * @param s the instance of StringBuilder to receive the script code
     */
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

    /**
     * Generate the java script object code of the give control object
     * @param control the control to generate its JS code
     * @return the java script code
     */
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

    /**
     * Generate the Java script code of the default properties of the form
     * @param frm the form to get the default properties from
     * @param s the instance of StringBuilder to include the code into
     */
    private void generateDefaultPropertiesCode(Form frm, StringBuilder s) {
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

    /**
     * Generate the java script code of a value. Several value types are supported, including
     * number, date, booleans, string, lists and objects
     * @param value the value to generate the js code
     * @return the js code generated
     */
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


    /**
     * Convert a list to java script code. Each item of the list will be converted to JS code
     * @param lst the list to generate the code
     * @return the js code representing a list of values
     */
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

    /**
     * Convert an object to java script code. Each property of the object will be converted
     * to java script code
     * @param obj the object to generate the js code from
     * @return the js code generated
     */
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
