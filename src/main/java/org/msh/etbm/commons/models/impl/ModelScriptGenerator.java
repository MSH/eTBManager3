package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.models.data.JSExprValue;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.util.List;
import java.util.Map;

/**
 * Generates the JavaScript script to be used when validating the model
 * Created by rmemoria on 2/7/16.
 */
public class ModelScriptGenerator {

    /**
     * Generate the JavaScript script for the given model
     * @param model model to generate the JS script
     * @return the JavaScript script
     */
    public String generate(Model model) {
        StringBuilder builder = new StringBuilder();

        builder.append("function ")
                .append(modelFunctionName(model))
                .append("() { return {");

        StringBuilder fieldsScript = generateFieldsScript(model);

        builder.append(fieldsScript);

        StringBuilder valBuilder = generateValidatorsScript(model.getValidators());

        if (valBuilder != null) {
            builder.append(", ").append(valBuilder);
        }

        builder.append("}; }");

        return builder.toString();
    }

    private StringBuilder generateValidatorsScript(List<Validator> validators) {
        if (validators == null || validators.size() == 0) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        s.append("validators: {");

        int index = 0;
        String delim = "";
        for (Validator validator: validators) {
            s.append(delim)
                    .append("v")
                    .append(index)
                    .append(": function() { return ")
                    .append(validator.getJsExpression())
                    .append("; }");

            delim = ", ";
            index++;
        }

        s.append("}");

        return s;
    }

    private StringBuilder generateFieldsScript(Model model) {
        StringBuilder s = new StringBuilder();
        s.append("fields: {");

        String delim = "";
        for (Field field: model.getFields()) {
            StringBuilder script = generateFieldScript(field);
            s.append(delim).append(script);
            delim = ", ";
        }

        s.append("}");

        return s;
    }

    private StringBuilder generateFieldScript(Field field) {
        StringBuilder s = new StringBuilder();
        s.append(field.getName()).append(": {");

        Map<String, Object> props = ObjectUtils.describeProperties(field);
        String delim = "";
        for (Map.Entry<String, Object> prop: props.entrySet()) {
            Object propType = prop.getValue();
            String script = propType instanceof JSExprValue ?
                    generatePropScript(prop.getKey(), (JSExprValue)propType) :
                    null;

            if (script != null) {
                s.append(delim).append(script);
                delim = ", ";
            }
        }

        // generate script to the custom validators
        StringBuilder srcValidators = generateValidatorsScript(field.getValidators());
        if (srcValidators != null) {
            s.append(delim).append(srcValidators);
            delim = ", ";
        }

        s.append("}");
        return s;
    }

    private String generatePropScript(String propName, JSExprValue propType) {
        if (!propType.isExpressionPresent()) {
            return null;
        }

        return propName + ": function() {return " + propType.getExpression() + "; }\n";
    }

    /**
     * Return the name of the function used in JavaScript to return the JS script schema
     * @param model
     * @return
     */
    public static String modelFunctionName(Model model) {
        return "model_" + model.getName();
    }
}
