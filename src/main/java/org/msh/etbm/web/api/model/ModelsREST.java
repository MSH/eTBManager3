package org.msh.etbm.web.api.model;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ModelManager;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * REST controllers to handle model retrieval and update
 *
 * Created by rmemoria on 3/1/17.
 */

@RestController
@RequestMapping("/api/models")
@Authenticated
public class ModelsREST {

    @Autowired
    ModelManager modelManager;

    /**
     * Return the list of available models
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<Item<String>> getAllModels() {
        return modelManager.getModels();
    }

    @RequestMapping(value = "/{modelId}", method = RequestMethod.GET)
    public Model getModel(@PathVariable String modelId) {
        CompiledModel cm = modelManager.getCompiled(modelId);
        return cm.getModel();
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public StandardResult updateModel(@RequestBody @NotNull Model model) {
        modelManager.update(model);
        return StandardResult.createSuccessResult();
    }
}
