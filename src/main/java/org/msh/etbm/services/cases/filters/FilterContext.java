package org.msh.etbm.services.cases.filters;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.ModelDAOFactory;

/**
 * Create a context to be used by filters in resource resolution
 *
 * Created by rmemoria on 17/8/16.
 */
public class FilterContext {

    private boolean initialization;

    private Messages messages;

    private ModelDAOFactory modelDAOFactory;

    /**
     * The default constructor
     * @param initialization true if the resources are being called during initialization,
     *                       or false if it is called remotely from the control
     * @param messages instance of {@link Messages} for localized message resolution
     */
    public FilterContext(boolean initialization, Messages messages, ModelDAOFactory modelDAOFactory) {
        this.initialization = initialization;
        this.messages = messages;
        this.modelDAOFactory = modelDAOFactory;
    }

    public boolean isInitialization() {
        return initialization;
    }

    public Messages getMessages() {
        return messages;
    }

    public ModelDAOFactory getModelDAOFactory() {
        return modelDAOFactory;
    }
}
