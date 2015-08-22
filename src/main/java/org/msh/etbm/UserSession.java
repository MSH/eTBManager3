package org.msh.etbm;

import org.msh.etbm.db.entities.UserLogin;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by rmemoria on 21/8/15.
 */
@Component
@Scope("request")
public class UserSession {

    /**
     * Store information about the user and its session
     */
    private UserLogin userLogin;

    /**
     * The selected language
     */
    private String language;


    public UserLogin getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(UserLogin userLogin) {
        this.userLogin = userLogin;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
