package org.msh.etbm.services.usersession;

import java.util.List;
import java.util.UUID;

/**
 * Basic information about the user session
 *
 * Created by rmemoria on 30/9/15.
 */
public class UserSession {

    /**
     * ID of the user login table
     */
    private UUID userLoginId;

    /**
     * ID of the user workspace table
     */
    private UUID userWorkspaceId;

    /**
     * Workspace of the current session
     */
    private UUID workspaceId;

    /**
     * Id of the current user
     */
    private UUID userId;

    /**
     * List of permissions that user is granted to
     */
    private List<String> permissions;

    /**
     * If true, user is a system administrator
     */
    private boolean administrator;

    /**
     * Return true if the given permission is granted to the user
     * @param perm the permission to check
     * @return true if permission is granted
     */
    public boolean isPermissionGranted(String perm) {
        return administrator?
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

    public UUID getUserWorkspaceId() {
        return userWorkspaceId;
    }

    public void setUserWorkspaceId(UUID userWorkspaceId) {
        this.userWorkspaceId = userWorkspaceId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
