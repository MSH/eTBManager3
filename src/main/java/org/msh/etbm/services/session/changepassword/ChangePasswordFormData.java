package org.msh.etbm.services.session.changepassword;

/**
 * Contain information about changing users password
 * Created by msantos on 27/6/16.
 */
public class ChangePasswordFormData {

    /**
     * The old password
     */
    private String password;

    /**
     * The new password
     */
    private String newPassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
