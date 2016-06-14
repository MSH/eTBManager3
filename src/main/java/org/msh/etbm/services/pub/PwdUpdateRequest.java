package org.msh.etbm.services.pub;

import org.msh.etbm.services.security.UserConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Request sent from the client to change user's password
 * Created by rmemoria on 13/6/16.
 */
public class PwdUpdateRequest {

    /**
     * The password reset request token
     */
    @NotNull
    private String token;

    /**
     * The new password
     */
    @NotNull
    @Pattern(regexp = UserConstants.PASSWORD_PATTERN)
    private String password;

    public PwdUpdateRequest(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public PwdUpdateRequest() {
        super();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
