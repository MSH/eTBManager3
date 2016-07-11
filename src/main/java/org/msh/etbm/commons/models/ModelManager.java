package org.msh.etbm.commons.models;

import org.msh.etbm.commons.models.data.Model;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmemoria on 1/7/16.
 */
@Service
public class ModelManager {

    private Map<String, Object> models = new HashMap<>();

    public void register(String id, Model model) {

    }

    public PreparedModel get(String modelId) {
        return null;
    }

    public void unregister(String id) {

    }

    public void update(Model model) {

    }
}
