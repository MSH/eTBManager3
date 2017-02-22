package org.msh.etbm.commons.models.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.db.entities.ModelData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * THis is a simple implementation of a component responsible for storing and restoring a model,
 * with support for caching
 *
 * Created by rmemoria on 30/7/16.
 */
@Component
public class ModelStoreService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ResourceLoader resourceLoader;


    /**
     * Retrieve a model by its ID and workspace ID. If model doesn' exist (invalid model)
     * system throws a {@link ModelException}
     * @param modelId the model ID
     * @return instance of {@link CompiledModel}
     */
    public Model get(String modelId) {
        Model model = loadFromDB(modelId);

        if (model == null) {
            model = loadFromResources(modelId);
        }

        if (model == null) {
            throw new IllegalArgumentException("Invalid model ID: " + modelId);
        }

        setIDs(model);

        return model;
    }


    /**
     * Return the list of available models in the system
     * @return list of {@link Item} objects containing model ID and its name
     */
    public List<Item<String>> getModels() {
        try {
            Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources("classpath*:/models/*.json");

            List<Item<String>> lst = new ArrayList<>();

            for (Resource res: resources) {
                String s = res.getFilename();
                s = s.substring(0, s.lastIndexOf('.'));
                lst.add(new Item<>(s, s));
            }

            return lst;
        } catch (IOException e) {
            throw new ModelException(e);
        }
    }

    /**
     * Update a model in its storage
     * @param model the model to be updated
     */
    @Transactional
    public void update(Model model) {
        String modelId = model.getName();
        ModelData data = loadModelData(modelId);

        if (data == null) {
            data = new ModelData();
            data.setId(modelId);
        }

        String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            throw new ModelException(e);
        }
        data.setJsonData(jsonData);

        entityManager.persist(data);
    }


    /**
     * Load the standard model from the system resource file
     * @param modelId the model ID
     * @return instance of {@link Model}
     */
    private Model loadFromResources(String modelId) {
        String resName = "/models/" + modelId + ".json";
        ClassPathResource res = new ClassPathResource(resName);

        try {
            Model model = objectMapper.readValue(res.getInputStream(), Model.class);
            return model;
        } catch (IOException e) {
            throw new ModelException(e);
        }
    }

    /**
     * Set the version and the field IDs used for model update operations
     * @param model
     */
    private void setIDs(Model model) {
        int index = 0;
        for (Field field: model.getFields()) {
            field.setId(index++);
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

        try {
            Model model = objectMapper.readValue(data.getJsonData(), Model.class);
            return model;
        } catch (IOException e) {
            throw new ModelException(e);
        }
    }


    private ModelData loadModelData(String modelId) {
        List<ModelData> lst = entityManager.createQuery("from ModelData " +
                "where id = :modelId")
                .setParameter("modelId", modelId)
                .setMaxResults(1)
                .getResultList();

        return lst.size() > 0 ? lst.get(0) : null;
    }
}
