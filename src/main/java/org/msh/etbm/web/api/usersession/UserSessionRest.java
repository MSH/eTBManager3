package org.msh.etbm.web.api.usersession;

import org.msh.etbm.services.usersession.*;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Rest API that returns information about the user session, like user name, workspace, permissions, etc.
 *
 * Created by rmemoria on 30/9/15.
 */
@RestController
@RequestMapping("api/sys")
public class UserSessionRest {

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    ChangeWorkspaceService changeWorkspaceService;

    /**
     * Get information about the user session
     * @param request object representing the client request
     * @return the session data
     */
    @Authenticated
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public UserSessionResponse getUserSession(HttpServletRequest request) {

        UserSession session = userRequestService.getUserSession();

        UserSessionResponse res = userSessionService.createClientResponse(session);

        return res;
    }

    /**
     * Change the current user workspace
     * @param request
     * @param userwsId
     * @return
     */
    @Authenticated
    @RequestMapping(value = "/changews/{userwsId}", method = RequestMethod.POST)
    public StandardResult changeWorkspace(HttpServletRequest request, @PathVariable UUID userwsId) {
        String ipAddr = request.getRemoteAddr();
        String app = request.getHeader("User-Agent");

        UUID newAuthToken = changeWorkspaceService.changeTo(userwsId, ipAddr, app);

        return new StandardResult(newAuthToken, null, newAuthToken != null);
    }
}
