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
    private String login;
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
