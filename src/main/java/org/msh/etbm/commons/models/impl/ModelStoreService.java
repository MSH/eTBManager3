package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.db.entities.ModelData;
import org.msh.etbm.db.entities.Workspace;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
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

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Retrieve a model by its ID and workspace ID. If model doesn' exist (invalid model)
     * system throws a {@link ModelException}
     * @param modelId the model ID
     * @return instance of {@link CompiledModel}
     */
    @Cacheable(cacheNames = CACHE_ID, key = "#modelId")
    public CompiledModel get(String modelId) {
        Model model = loadFromDB(modelId);

        if (model == null) {
            model = loadFromResources(modelId);
        }

        CompiledModel compiledModel = new CompiledModel(model);

        return compiledModel;
    }


    @Transactional
    @CachePut(cacheNames = CACHE_ID, key = "#modelId")
    public void update(String modelId, Model model) {
        ModelData data = loadModelData(modelId);

        if (data == null) {
            String jsonData = JsonParser.objectToJSONString(model, false);

            data = new ModelData();
            data.setId(modelId);
            data.setJsonData(jsonData);
        }

        entityManager.persist(data);
    }


    /**
     * Load the standard model from the system resource file
     * @param modelId the model ID
     * @return instance of {@link Model}
     */
    private Model loadFromResources(String modelId) {
        String resName = "/models/" + modelId + ".json";
        JsonModelParser parser = new JsonModelParser();
        ClassPathResource res = new ClassPathResource(resName);

        try {
            return parser.parse(res.getInputStream());
        } catch (IOException e) {
            throw new ModelException(e);
        }
    }

    /**
     * Load a customized model from the database
     * @param modelId the model ID
     * @return instance of {@link Model}
     */
    private Model loadFromDB(String modelId) {
        ModelData data = loadModelData(modelId);

        if (data == null) {
            return null;
        }

        JsonModelParser parser = new JsonModelParser();
        return parser.parse(data.getJsonData());
    }


    private ModelData loadModelData(String modelId) {
        List<ModelData> lst = entityManager.createQuery("from ModelData where id = :modelId")
                .setParameter("modelId", modelId)
                .setMaxResults(1)
                .getResultList();

        return lst.size() > 0 ? lst.get(0) : null;
    }
}
