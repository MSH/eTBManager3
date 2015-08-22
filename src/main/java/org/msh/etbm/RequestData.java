package org.msh.etbm;

import org.msh.etbm.db.entities.UserSession;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by rmemoria on 21/8/15.
 */
@Component
@Scope("request")
public class RequestData {

    /**
     * Store information about the user and its session
     */
    private UserSession userSession;

    /**
     * The selected language
     */
    private String language;


    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
