package org.msh.etbm.commons.models.impl;

import org.msh.etbm.Messages;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * The validation context offers the possibility to access application common used components,
 * like the DataSource and {@link Messages}
 *
 * Created by rmemoria on 13/7/16.
 */
public class ModelResources {

    private DataSource dataSource;
    private Messages messages;
    private UUID workspaceId;

    /**
     * True if system was initialized in development mode
     */
    private boolean development;

    public ModelResources(DataSource dataSource, Messages messages, UUID workspaceId, boolean development) {
        this.dataSource = dataSource;
        this.messages = messages;
        this.workspaceId = workspaceId;
        this.development = development;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Messages getMessages() {
        return messages;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }
}
