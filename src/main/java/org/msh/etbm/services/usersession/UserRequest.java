package org.msh.etbm.services.usersession;

import org.msh.etbm.db.dto.UserWorkspaceDTO;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Basic information about the user session
 *
 * Created by rmemoria on 30/9/15.
 */
@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRequest {

    /**
     * ID of the user login table
     */
    private UUID userLoginId;


    /**
     * List of permissions that user is granted to
     */
    private List<String> permissions;

    /**
     * Object containing information about the user and its workspace
     */
    private UserWorkspaceDTO userWorkspace;

    /**
     * If true, the current request is being executed under a declared command
     */
    private boolean commandExecuting;


    /**
     * Check if the user was properly authenticated
     * @return true if the user is authenticated
     */
    public boolean isAuthenticated() {
        return userWorkspace != null;
    }


    /**
     * Return true if the given permission is granted to the user
     * @param perm the permission to check
     * @return true if permission is granted
     */
    public boolean isPermissionGranted(String perm) {
        if (userWorkspace == null) {
            return false;
        }
        return userWorkspace.isAdministrator()?
                true:
                permissions != null && permissions.contains(perm);
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public UUID getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(UUID userLoginId) {
        this.userLoginId = userLoginId;
    }

    public UserWorkspaceDTO getUserWorkspace() {
        return userWorkspace;
    }

    public void setUserWorkspace(UserWorkspaceDTO userWorkspace) {
        this.userWorkspace = userWorkspace;
    }

    public boolean isCommandExecuting() {
        return commandExecuting;
    }

    public void setCommandExecuting(boolean commandExecuting) {
        this.commandExecuting = commandExecuting;
    }
}
