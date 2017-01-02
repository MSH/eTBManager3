package org.msh.etbm.commons.forms.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.forms.FormException;
import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.controls.ValuedControl;
import org.msh.etbm.commons.forms.data.DataModel;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.models.ModelManager;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generate java script code to be sent to the client (browser) with a form schema.
 * The code generated is wrapped by a function and can be executed with an eval function
 * in the javascript environment.
 *
 * Created by rmemoria on 26/7/16.
 */
@Service
public class JavaScriptFormGenerator {

    @Autowired
    Messages messages;

    @Autowired
    ModelManager modelManager;


    /**
     * Generate the java script code with a form schema to be executed by the browser
     * @param form the form data, instance of {@link Form}
     * @param funcName the name of the function to be declared as the entry point function in the script
     * @return the java script code
     */
    public String generate(Form form, String funcName) {
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

        if (frm.getTitle() != null) {
            generateTitle(frm.getTitle(), s);
            delim = ",\n";
        }

        if (frm.getDefaultProperties() != null) {
            s.append(delim);
            generateDefaultPropertiesCode(frm, s);
            delim = ",\n";
        }

        if (frm.getControls() != null) {
            s.append(delim);
            s.append(createControlsScript(frm.getControls(), frm.getDataModel()));
            delim = ",\n";
        }

        if (frm.getValidators() != null) {
            s.append(delim).append(" validators:").append(convertValue(frm.getValidators()));
            delim = ",\n";
        }
        s.append("\n}");
    }


    private void generateTitle(JSFuncValue<String> title, StringBuilder s) {
        s.append("title: ").append(convertValue(title));
    }

    /**
     * Generate a java script code for the list of controls in the form
     * @param controls the list of controls
     * @param dm the instance of {@link DataModel} related to the control
     */
    private String createControlsScript(List<Control> controls, DataModel dm) {
        StringBuilder s = new StringBuilder();

        s.append("controls:[");
        String delim = "";
        for (Control control: controls) {
            s.append(delim);
            s.append(generateControlScript(control, dm));
            delim = ",";
        }
        s.append("\n]");

        return s.toString();
    }

    /**
     * Generate the java script object code of the give control object
     * @param control the control to generate its JS code
     * @param dm the instance of {@link DataModel} related to the control
     * @return the java script code
     */
    private String generateControlScript(Control control, DataModel dm) {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        try {
            Map<String, Object> props = collectControlProperties(control, dm);

            // mount the list of properties
            String delim = "";
            for (Map.Entry<String, Object> entry: props.entrySet()) {
                if (entry.getValue() != null) {
                    if (entry.getKey().equals("controls")) {
                        s.append(delim).append(createControlsScript((List<Control>)entry.getValue(), dm));
                        delim = ",\n";
                    } else {
                        String sval = convertValue(entry.getValue());

                        if (sval != null) {
                            s.append(delim).append(entry.getKey()).append(": ").append(sval);
                            delim = ",\n";
                        }

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
     * Collect all properties from the control that must be available in the java script schema
     *
     * @param control
     * @param dm
     * @return
     */
    private Map<String, Object> collectControlProperties(Control control, DataModel dm) {
        try {
            Map<String, Object> props = selectPropertiesToGenerate(control);

            if (control instanceof ValuedControl) {
                ValuedControl vc = (ValuedControl)control;
                Field field = vc.getField();

                // check if control has a field
                Map<String, Object> fprops = field != null ? selectPropertiesToGenerate(field) : null;

                Field modelField = dm.getFieldModel(modelManager, vc.getProperty());

                // check if there is an equivalent field
                if (modelField != null) {
                    // mix model field into properties of the control field
                    Map<String, Object> fprops2 = selectPropertiesToGenerate(modelField);

                    if (fprops != null) {
                        for (Map.Entry<String, Object> entry: fprops2.entrySet()) {
                            String propName = entry.getKey();

                            if (fprops.get(propName) == null) {
                                fprops.put(propName, entry.getValue());
                            }
                        }
                    } else {
                        fprops = fprops2;
                    }
                }

                // mix properties from control and field
                props.putAll(fprops);
            }

            return props;
        } catch (Exception e) {
            throw new FormException(e);
        }
    }


    /**
     * Select the properties that will be generated to javascript
     * @param bean The bean to get the properties from
     * @return
     */
    private Map<String, Object> selectPropertiesToGenerate(Object bean) {
        // get the properties
        Map<String, Object> props;
        try {
            props = PropertyUtils.describe(bean);
        } catch (Exception e) {
            throw new FormException(e);
        }

        Map<String, Object> res = new HashMap<>();

        for (Map.Entry<String, Object> entry: props.entrySet()) {
            String propName = entry.getKey();
            Object value = entry.getValue();

            if ("class".equals(propName) || value == null) {
                continue;
            }

            // initially, if there is no field (but a get method), the value is not converted to JS
            java.lang.reflect.Field f = ObjectUtils.findField(bean.getClass(), propName);
            if (f == null) {
                continue;
            }

            if (f.getAnnotation(JsonIgnore.class) == null) {
                res.put(propName, entry.getValue());
            }
        }

        return res;
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
     * @param val the value to generate the js code
     * @return the js code generated
     */
    private String convertValue(Object val) {
        Object value = val;

        if (value instanceof JSGeneratorValueWrapper) {
            value = ((JSGeneratorValueWrapper)value).getValueToGenerateJSCode();
        }

        if (value == null) {
            return null;
        }

        if (value instanceof String) {
            String txt = messages.eval((String)value);
            txt = StringEscapeUtils.escapeEcmaScript(txt);
            return "'" + txt + "'";
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
                String func = messages.eval(f.getFunction());
                return "function(doc) { return " + func + "; }";
            }
            return convertValue(f.getValue());
        }

        if (value instanceof Validator) {
            return convertValidator((Validator)value);
        }

        if (value instanceof List) {
            List lst = (List)value;
            return convertList(lst);
        }

        return convertObject(value);
    }


    /**
     * Convert a validator to a java script object
     * @param validator
     * @return
     */
    private String convertValidator(Validator validator) {
        StringBuilder s = new StringBuilder();
        s.append("{ rule: function() { return ").append(validator.getRule().getExpression()).append("; }");

        if (validator.getMessage() != null) {
            String msg = messages.eval(validator.getMessage());
            s.append(", message: \"").append(msg).append('"');
        }
        s.append('}');

        return s.toString();
    }


    /**
     * Convert a string value to a java script function
     * @param expression
     * @return
     */
    private String convertToJSFunction(String expression) {
        return "function() { return " + expression + "; }";
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
            Map<String, Object> props = selectPropertiesToGenerate(obj);

            String delim = "";
            for (Map.Entry<String, Object> prop: props.entrySet()) {
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
