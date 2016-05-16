package org.msh.etbm.services.usersettings;

import java.util.Optional;

/**
 * Contain information about user settings to be edited in a form
 * Created by rmemoria on 14/5/16.
 */
public class UserSettingsFormData {

    /**
     * The user name
     */
    private Optional<String> name;

    /**
     * The user mobile number
     */
    private Optional<String> mobile;

    /**
     * The user e-mail address
     */
    private Optional<String> email;

    /**
     * The user time zone
     */
    private Optional<String> timeZone;


    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getMobile() {
        return mobile;
    }

    public void setMobile(Optional<String> mobile) {
        this.mobile = mobile;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public Optional<String> getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(Optional<String> timeZone) {
        this.timeZone = timeZone;
    }

}
