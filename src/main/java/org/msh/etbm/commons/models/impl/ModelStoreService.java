package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.data.Model;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * THis is a simple implementation of a component responsible for storing and restoring a model,
 * with support for caching
 *
 * Created by rmemoria on 30/7/16.
 */
@Component
public class ModelStoreService {

    public static final String CACHE_ID = "models";

    @Cacheable(cacheNames = CACHE_ID)
    public CompiledModel get(String modelId, UUID workspaceId) {
        String resName = "/models/" + modelId + ".json";
        Model model = JsonParser.parseResource(resName, Model.class);

        CompiledModel compiledModel = new CompiledModel(model);

        return compiledModel;
    }
}
