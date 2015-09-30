package org.msh.etbm.web.api.authentication;

import javax.validation.constraints.NotNull;

/**
 * Request used to return the list of workspaces of a given user
 *
 * Created by rmemoria on 29/9/15.
 */
public class WorkspacesRequest {
    /**
     * User name
     */
    @NotNull
    private String username;

    /**
     * User password
     */
    @NotNull
    private String password;

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
}
