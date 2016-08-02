package org.msh.etbm.commons.forms.data;

import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelManager;
import org.msh.etbm.commons.models.data.fields.Field;

/**
 * Represent a data model used in a form with reference to a single variable, i.e, the own variable
 * is the context to resolve the properties
 *
 * Created by rmemoria on 30/7/16.
 */
public class SingleDataModel implements DataModel {

    private String modelId;

    public SingleDataModel(String modelId) {
        this.modelId = modelId;
    }

    @Override
    public Field getFieldModel(ModelManager modelManager, String fieldRef) {
        CompiledModel compModel = modelManager.get(modelId);

        return compModel.getModel().findFieldByName(fieldRef);
    }

    public String getModelId() {
        return modelId;
    }
}
