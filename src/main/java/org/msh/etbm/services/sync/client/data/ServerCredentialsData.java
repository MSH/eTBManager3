package org.msh.etbm.services.sync.client.data;

import java.util.UUID;

/**
 * Data class to initialize an off-line mode instance
 * Created by Mauricio on 21/11/2016.
 */
public class ServerCredentialsData {

    private String parentServerUrl;
    private String username;
    private String password;
    private UUID workspaceId;

    public ServerCredentialsData() {
        super();
    }

    public ServerCredentialsData(String username, String password, UUID workspaceId) {
        this.username = username;
        this.password = password;
        this.workspaceId = workspaceId;
    }

    public String getParentServerUrl() {
        return parentServerUrl;
    }

    public void setParentServerUrl(String parentServerUrl) {
        this.parentServerUrl = parentServerUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }
}
