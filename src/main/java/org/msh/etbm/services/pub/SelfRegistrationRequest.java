package org.msh.etbm.services.pub;

import org.hibernate.validator.constraints.Email;
import org.msh.etbm.services.security.UserConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by rmemoria on 13/6/16.
 */
public class SelfRegistrationRequest {

    @NotNull
    private String name;

    @NotNull
    private String login;

    @NotNull
    @Email
    @Pattern(regexp = UserConstants.EMAIL_PATTERN, message = "NotValidEmail")
    private String email;

    /**
     * The user organization
     */
    private String organization;

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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }
}
