package org.msh.etbm.services.session.usersession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service to change the user workspace
 * 
 * Created by rmemoria on 31/12/15.
 */
@Service
public class ChangeWorkspaceService {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Change the user workspace to another workspace
     * @param wsuserId the ID of the user workspace
     */
    public UUID changeTo(UUID wsuserId, String ipAddr, String appName) {
        // start a new session
        UUID newAuthToken = userSessionService.beginSession(wsuserId, ipAddr, appName);

        // update user session
        UserSession session = userSessionService.recoverSession(newAuthToken);
        userRequestService.setUserSession(session);

        // end current session
        userSessionService.endSession(userRequestService.getAuthToken());

        // return new token
        return newAuthToken;
    }
}
