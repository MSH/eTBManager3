package org.msh.etbm.services.authentication;

import org.msh.etbm.db.entities.UserSession;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by rmemoria on 16/8/15.
 */
@Service
public class AuthenticationService {

    @PersistenceContext
    EntityManager entityManager;

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
}
