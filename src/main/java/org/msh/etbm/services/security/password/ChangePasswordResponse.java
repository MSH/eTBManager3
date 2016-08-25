package org.msh.etbm.services.security.password;

import java.util.UUID;

/**
 * Created by Mauricio on 26/07/2016.
 */
public class ChangePasswordResponse {
    private UUID userModifiedId;
    private String userModifiedName;
    private String detail;

    public ChangePasswordResponse(UUID userModifiedId, String userModifiedName, String detail) {
        this.userModifiedId = userModifiedId;
        this.userModifiedName = userModifiedName;
        this.detail = detail;
    }

    public UUID getUserModifiedId() {
        return userModifiedId;
    }

    public void setUserModifiedId(UUID userModifiedId) {
        this.userModifiedId = userModifiedId;
    }

    public String getUserModifiedName() {
        return userModifiedName;
    }

    public void setUserModifiedName(String userModifiedName) {
        this.userModifiedName = userModifiedName;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
