package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.impl.ModelStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manage the instance of {@link Model} available in the current workspace
 *
 * Created by rmemoria on 1/7/16.
 */
@Service
public class ModelManager {

    @Autowired
    ModelStoreService modelStoreService;

    private Map<UUID, Map<String, CompiledModel>> models = new HashMap<>();

    public CompiledModel get(String modelId) {
        return modelStoreService.get(modelId);
    }

}
