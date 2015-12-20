package org.msh.etbm.web.api.usersession;

import org.msh.etbm.services.usersession.UserRequestService;
import org.msh.etbm.services.usersession.UserSession;
import org.msh.etbm.services.usersession.UserSessionResponse;
import org.msh.etbm.services.usersession.UserSessionService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    @Authenticated
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public UserSessionResponse getUserSession(HttpServletRequest request) {

        UserSession session = userRequestService.getUserSession();

        UserSessionResponse res = userSessionService.createClientResponse(session);

        return res;
    }
}
