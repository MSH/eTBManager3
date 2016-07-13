package org.msh.etbm.commons.models;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.models.data.Model;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 1/7/16.
 */
@Service
public class ModelManager {

    // create a fake workspace ID just to represent the generic models stored in cache
    public static final UUID GENERIC_WS = new UUID(0, 0);

    @Value("${development:false}")
    boolean development;

    private Map<UUID, Map<String, CompiledModel>> models = new HashMap<>();

    public CompiledModel get(String modelId, UUID workspaceId) {
        // TODO Implement model recover by workspace (by now just the generic)
        return findGenericModel(modelId);
    }

    private CompiledModel findGenericModel(String modelId) {
        // check if model is in the cache
        CompiledModel compModel = findModelInCache(modelId, GENERIC_WS);
        if (compModel != null) {
            return compModel;
        }

        Model model = loadFromResources(modelId);
        CompiledModel compiledModel = new CompiledModel(model);

        // if in development mode, doesn't store model in cache
        if (development) {
            return compiledModel;
        }

        addModelToCache(compiledModel, GENERIC_WS);

        return compModel;
    }


    /**
     * Add model to the memory cache in order to speed up process
     * @param compiledModel
     * @param wsId
     */
    private void addModelToCache(CompiledModel compiledModel, UUID wsId) {
        Map<String, CompiledModel> wsModels = models.get(wsId);

        if (wsModels == null) {
            wsModels = new HashMap<>();
            models.put(wsId, wsModels);
        }

        wsModels.put(compiledModel.getModel().getName(), compiledModel);
    }


    /**
     * Search for a model in the cache by its model ID and workspace ID
     * @param modeId
     * @param workspaceId
     * @return
     */
    private CompiledModel findModelInCache(String modeId, UUID workspaceId) {
        Map<String, CompiledModel> wsmodels = models.get(workspaceId);
        if (wsmodels == null) {
            return null;
        }
        return wsmodels.get(modeId);
    }

    private Model loadFromResources(String modelId) {
        String resName = "/models/" + modelId + ".json";
        Model model = JsonParser.parseResource(resName, Model.class);
        return model;
    }

    public void register(Model model, UUID workspaceId) {

    }

    public void unregister(String id) {

    }

    public void update(Model model) {

    }
}
