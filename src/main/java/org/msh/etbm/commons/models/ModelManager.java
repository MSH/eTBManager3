package org.msh.etbm.commons.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.impl.ModelStoreService;
import org.msh.etbm.commons.models.json.ModelJacksonModule;
import org.msh.etbm.commons.models.tableupdate.SchemaUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Manage the instance of {@link Model} available in the current workspace
 *
 * Created by rmemoria on 1/7/16.
 */
@Service
public class ModelManager {

    @Autowired
    ModelStoreService modelStoreService;

    @Autowired
    SchemaUpdateService schemaUpdateService;


    // A simple local cache of compiled models
    // this local cache will avoid compilation of the model to JS on any call
    private Map<String, CompiledModel> models = new HashMap<>();

    public ModelManager(ObjectMapper objectMapper) {
        objectMapper.registerModule(new ModelJacksonModule());
    }

    /**
     * Get an instance of the {@link CompiledModel} from the given model ID. The {@link CompiledModel}
     * can be used for validation of a document against the model
     * @param modelId the ID of the model
     * @return
     */
    public CompiledModel getCompiled(String modelId) {
        Model model = modelStoreService.get(modelId);

        // check if compiled model is available
        CompiledModel compModel = models.get(modelId);

        // check if there is no compiled model or if the compiled model is
        // with a different version of the model from the data store
        if (compModel == null || compModel.getModel().getVersion() != model.getVersion()) {
            compModel = new CompiledModel(model);
            models.put(modelId, compModel);
        }

        return compModel;
    }

    /**
     * Return a copy of the model
     * @param modelId the model ID
     * @return the instance of {@link Model}
     */
    public Model get(String modelId) {
        return modelStoreService.get(modelId);
    }

    /**
     * Update a model. This operation also updates the table structure, if necessary
     * @param model The model to be updated
     */
    public void update(Model model) {
        Model currentModel = modelStoreService.get(model.getName());
        schemaUpdateService.update(currentModel, model);

        modelStoreService.update(model);
    }
}
