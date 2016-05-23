package org.msh.etbm.web.api.usersession;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.services.usersession.*;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
        UserSessionResponse res = userSessionService.createClientResponse();

        return res;
    }

    /**
     * Change the current user workspace
     * @param request object containing information about the HTTP request
     * @param userwsId the ID of the selected workspace
     * @return
     */
    @Authenticated
    @RequestMapping(value = "/changews/{userwsId}", method = RequestMethod.POST)
    public ChangeWsResponse changeWorkspace(HttpServletRequest request, @PathVariable UUID userwsId) {
        String ipAddr = request.getRemoteAddr();
        String app = request.getHeader("User-Agent");

        // change the workspace
        UUID newAuthToken = changeWorkspaceService.changeTo(userwsId, ipAddr, app);

        // convert it to a client response
        UserSessionResponse res = userSessionService.createClientResponse();

        // send it back to the client
        return new ChangeWsResponse(newAuthToken, res);
    }


    /**
     * Return the list of user workspaces
     * @return list of workspaces
     */
    @Authenticated
    @RequestMapping(value = "/workspaces", method = RequestMethod.POST)
    public List<SynchronizableItem> getUserWorkspaces() {
        return userSessionService.getUserWorkspaces();
    }
}
