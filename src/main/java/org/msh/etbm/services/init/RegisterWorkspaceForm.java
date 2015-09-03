package org.msh.etbm.services.init;

import org.msh.etbm.services.users.UserConstants;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Information necessary to register a new workspace in the initialization of the system
 * Created by rmemoria on 1/9/15.
 */
public class RegisterWorkspaceForm {

    /**
     * The workspace name to be displayed on the screen
     */
    @Size(min=3,max=100)
    @NotNull
    private String workspaceName;

    /**
     * The workspace description
     */
    @Size(min=3,max=250)
    private String workspaceDescription;

    /**
     * The administrator password
     */
    @Size(max=50)
    @NotNull
    @Pattern(regexp = UserConstants.PASSWORD_PATTERN)
    private String adminPassword;

    /**
     * The administrator e-mail address
     */
    @Size(max=100)
    @NotNull
    @Pattern(regexp = UserConstants.EMAIL_PATTERN)
    private String adminEmail;


    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public String getWorkspaceDescription() {
        return workspaceDescription;
    }

    public void setWorkspaceDescription(String workspaceDescription) {
        this.workspaceDescription = workspaceDescription;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }
}
