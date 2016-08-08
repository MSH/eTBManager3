package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        JsonModelParser2 parser = new JsonModelParser2();
        ClassPathResource res = new ClassPathResource(resName);

        Model model = null;
        try {
            model = parser.parse(res.getInputStream());
        } catch (IOException e) {
            throw new ModelException(e);
        }

        CompiledModel compiledModel = new CompiledModel(model);

        return compiledModel;
    }
}
