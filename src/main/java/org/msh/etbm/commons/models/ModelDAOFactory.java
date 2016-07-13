package org.msh.etbm.commons.models;

import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * Factory service to create instances of {@link ModelDAO} class. This service must be
 * used in order to execute CRUD operations in a {@link org.msh.etbm.commons.models.data.Model} representation.
 *
 * Created by rmemoria on 13/7/16.
 */
@Service
public class ModelDAOFactory {

    @Autowired
    ModelManager modelManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    DataSource dataSource;

    /**
     * Create a new model DAO from a model name
     * @param modelName the name of the model to search for
     * @return instance of {@link ModelDAO}
     */
    public ModelDAO create(String modelName) {
        UUID wsId = userRequestService.getUserSession().getWorkspaceId();
        CompiledModel compiledModel = modelManager.get(modelName, wsId);

        return new ModelDAO(compiledModel, dataSource, wsId);
    }
}
