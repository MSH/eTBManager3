package org.msh.etbm.web.api.usersession;

import org.msh.etbm.services.usersession.UserSessionService;
import org.msh.etbm.web.api.authentication.AuthConstants;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
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
    UserSessionService userSessionService;

    @Authenticated
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public UserSessionRequest getUserSession(HttpServletRequest request) {
        // get the authentication token stored in the request
        UUID authToken = (UUID)request.getAttribute(AuthConstants.AUTH_TOKEN);

        if (authToken == null) {
            throw new IllegalArgumentException("Authorization token not found in the request");
        }

        userSessionService.getSessionByAuthToken(authToken);

        UserSessionRequest req = new UserSessionRequest();

        return req;
    }
}
