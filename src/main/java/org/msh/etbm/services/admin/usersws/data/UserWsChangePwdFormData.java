package org.msh.etbm.services.admin.usersws.data;

import java.util.UUID;

/**
 * Contain information about changing password of another user
 * Created by msantos on 28/6/16.
 */
public class UserWsChangePwdFormData {

    /**
     * The user that will have its password modified
     */
    private UUID userWsId;

    /**
     * The old password
     */
    private String newPassword;

    public UUID getUserWsId() {
        return userWsId;
    }

    public void setUserWsId(UUID userId) {
        this.userWsId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
