package org.msh.etbm.services.pub;

/**
 * Data returned to the client from the {@link ForgotPwdService} to validate the
 * change request token
 *
 * Created by rmemoria on 13/6/16.
 */
public class PwdResetTokenResponse {

    /**
     * The user name
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
