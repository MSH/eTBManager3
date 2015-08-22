package org.msh.etbm.rest.authentication;

import java.util.UUID;

/**
 * Login form sent by the client in JSON format
 * Created by rmemoria on 21/8/15.
 */
public class LoginForm {

    private String username;
    private String password;
    private UUID workspaceId;

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
