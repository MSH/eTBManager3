package org.msh.etbm.services.authentication;

import org.msh.etbm.RequestData;
import org.msh.etbm.db.entities.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 16/8/15.
 */
@Service
public class AuthenticationService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * Return the user session by its token, or return null if no session is found
     * @param authToken
     * @return
     */
    public UserSession getSessionByToken(String authToken) {
        if (authToken == null || authToken.isEmpty()) {
            return null;
        }

        List<UserSession> lst = entityManager
                .createQuery("from UserSession where sessionId = :id")
                .setParameter("id", authToken)
                .setMaxResults(1)
                .getResultList();

        return lst.size() > 0? lst.get(0): null;
    }


    /**
     * Authenticate the user by its authentication token
     * @param token the authentication token
     * @return true if user was authenticated
     */
    public boolean authenticateByToken(String token) {
        UserSession userSession = getSessionByToken(token);
        if (userSession == null) {
            return false;
        }

        // set user information to be available in the system
        RequestData req = applicationContext.getBean(RequestData.class);
        req.setUserSession(userSession);
        return true;
    }


    /**
     * Authenticate a user by its user name, password and workspace
     * @param username the user name
     * @param password the user password
     * @param workspaceId the workspace to log into
     * @return true if the user was authenticated
     */
    public boolean authenticate(String username, String password, UUID workspaceId) {
        return false;
    }
}
