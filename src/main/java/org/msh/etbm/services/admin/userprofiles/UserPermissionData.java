package org.msh.etbm.services.admin.userprofiles;

/**
 * Store information about a permission of a user profile, used for displaying and in form data
 * Created by rmemoria on 26/1/16.
 */
public class UserPermissionData {

    private String permission;
    private boolean canExecute;
    private boolean canChange;


    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isCanExecute() {
        return canExecute;
    }

    public void setCanExecute(boolean canExecute) {
        this.canExecute = canExecute;
    }

    public boolean isCanChange() {
        return canChange;
    }

    public void setCanChange(boolean canChange) {
        this.canChange = canChange;
    }
}
