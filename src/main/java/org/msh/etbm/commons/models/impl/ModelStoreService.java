package org.msh.etbm.commons.models.impl;

import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.db.entities.ModelData;
import org.msh.etbm.db.entities.Workspace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
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
     * @param workspaceId the workspace ID to get the model from
     * @return instance of {@link CompiledModel}
     */
    @Cacheable(cacheNames = CACHE_ID, key = "#modelId + #workspaceId.toString()")
    public CompiledModel get(String modelId, UUID workspaceId) {
        Model model = loadFromDB(modelId, workspaceId);

        if (model == null) {
            model = loadFromResources(modelId);
        }

        CompiledModel compiledModel = new CompiledModel(model);

        return compiledModel;
    }


    @Transactional
    @CachePut(cacheNames = CACHE_ID, key = "#modelId + #workspaceId.toString()")
    public void update(String modelId, UUID workspaceId, Model model) {
        ModelData data = loadModelData(modelId, workspaceId);

        if (data == null) {
            Workspace ws = entityManager.find(Workspace.class, modelId);
            String jsonData = JsonParser.objectToJSONString(model, false);

            data = new ModelData();
            data.setModelId(modelId);
            data.setWorkspace(ws);
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
     * @param workspaceId the workspace ID
     * @return instance of {@link Model}
     */
    private Model loadFromDB(String modelId, UUID workspaceId) {
        ModelData data = loadModelData(modelId, workspaceId);

        if (data == null) {
            return null;
        }

        JsonModelParser parser = new JsonModelParser();
        return parser.parse(data.getJsonData());
    }


    private ModelData loadModelData(String modelId, UUID workspaceId) {
        List<ModelData> lst = entityManager.createQuery("from ModelData where workspace.id = :wsid " +
                "and modelId = :modelId")
                .setParameter("wsid", workspaceId)
                .setParameter("modelId", modelId)
                .setMaxResults(1)
                .getResultList();

        return lst.size() > 0 ? lst.get(0) : null;
    }
}
