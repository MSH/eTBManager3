package org.msh.etbm.services.usersession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Service to return information about the on-going user session under the current request
 *
 * Created by rmemoria on 30/9/15.
 */
@Service
public class UserSessionService {

    public static final String SESSION_KEY = "userSession";

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Return the user session information by its authentication token
     * @param authToken the authentication token
     * @return instance of the user session, or null if authentication token is invalid
     */
    public UserSession getSessionByAuthToken(UUID authToken) {

        UserSession userSession = entityManager.find(UserSession.class, authToken);
        return null;
    }


    /**
     * Return the current user session information of the given request
     * @param request the current HTTP request
     * @return the information about the user
     */
    @Bean
    @Scope("request")
    public UserSession userSession(HttpServletRequest request) {
        return (UserSession) request.getAttribute(SESSION_KEY);
    }
}
