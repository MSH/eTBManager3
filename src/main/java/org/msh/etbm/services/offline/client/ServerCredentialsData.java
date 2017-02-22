package org.msh.etbm.services.offline.client;

import java.util.UUID;

/**
 * Data class to store credentials of parent server
 * Created by Mauricio on 21/11/2016.
 */
public class ServerCredentialsData {

    /**
     * The URL od parent server
     */
    private String parentServerUrl;

    /**
     * The user name on parent server
     */
    private String username;

    /**
     * The user password on parent server
     */
    private String password;

    /**
     * The workspace id that the user is logging in
     */
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
