package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelManager;
import org.msh.etbm.commons.models.data.fields.Field;

import java.util.Map;

/**
 * Implementation of the form data model with support for several variables, each one representing
 * a model. When resolving the values, the first reference in the value name is the variable
 *
 * Created by rmemoria on 30/7/16.
 */
public class MultipleDataModel implements DataModel {

    private Map<String, String> variables;

    /**
     * Default constructor
     * @param variables the list of variable names and its corresponding model ID
     */
    public MultipleDataModel(Map<String, String> variables) {
        this.variables = variables;
    }

    @Override
    public Field getFieldModel(ModelManager modelManager, String fieldRef) {
        int pos = fieldRef.indexOf('.');
        if (pos == -1) {
            return null;
        }

        String varname = fieldRef.substring(0, pos);
        String value = fieldRef.substring(pos + 1);

        String modelId = variables.get(varname);
        if (modelId == null) {
            return null;
        }

        CompiledModel compModel = modelManager.get(modelId);
        if (compModel == null) {
            return null;
        }

        return compModel.getModel().findFieldByName(value);
    }
}
