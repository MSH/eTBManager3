package org.msh.etbm.web.api.init;

import javax.validation.constraints.NotNull;

/**
 * Created by rmemoria on 22/8/15.
 */
public class InitWorkspaceForm {

    /**
     * Workspace name
     */
    @NotNull
    private String wsName;

    /**
     * Administrator password
     */
    @NotNull
    private String adminPassword;

    /**
     * Administrator e-mail address
     */
    @NotNull
    private String adminEmail;


    public String getWsName() {
        return wsName;
    }

    public void setWsName(String wsName) {
        this.wsName = wsName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }
}
