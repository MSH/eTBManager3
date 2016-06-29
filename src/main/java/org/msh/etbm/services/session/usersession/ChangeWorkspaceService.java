package org.msh.etbm.services.session.usersession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Service to change the user workspace
 * <p>
 * Created by rmemoria on 31/12/15.
 */
@Service
public class ChangeWorkspaceService {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserRequestService userRequestService;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Change the user workspace to another workspace
     *
     * @param wsuserId the ID of the user workspace
     */
    @Transactional
    public UUID changeTo(UUID wsuserId, String ipAddr, String appName) {
        // start a new session
        UUID newAuthToken = userSessionService.beginSession(wsuserId, ipAddr, appName);

        // update user session
        UserSession session = userSessionService.recoverSession(newAuthToken);
        userRequestService.setUserSession(session);

        // end current session
        userSessionService.endSession(userRequestService.getAuthToken());

        updateDefaultWorkspace(session);

        // return new token
        return newAuthToken;
    }

    /**
     * Update the default workspace in use by the user
     *
     * @param session the current user session
     */
    private void updateDefaultWorkspace(UserSession session) {
        entityManager.createQuery("update User set defaultWorkspace.id = :wsid where id = :id")
                .setParameter("wsid", session.getUserWorkspaceId())
                .setParameter("id", session.getUserId())
                .executeUpdate();
    }
}
