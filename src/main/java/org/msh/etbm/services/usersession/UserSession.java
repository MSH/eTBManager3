package org.msh.etbm.services.usersession;

import java.util.Arrays;

/**
 * Created by rmemoria on 30/9/15.
 */
public class UserSession {
    private String[] permissions;

    /**
     * Return true if the given permission is granted to the user
     * @param perm the permission to check
     * @return true if permission is granted
     */
    public boolean isPermissionGranted(String perm) {
        return permissions != null && Arrays.binarySearch(permissions, perm) >= 0;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
