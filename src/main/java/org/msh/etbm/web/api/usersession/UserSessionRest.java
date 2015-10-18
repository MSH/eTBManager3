package org.msh.etbm.web.api.usersession;

import org.msh.etbm.db.dto.UserWorkspaceDTO;
import org.msh.etbm.services.usersession.UserSession;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

/**
 * Rest API that returns information about the user session, like user name, workspace, permissions, etc.
 *
 * Created by rmemoria on 30/9/15.
 */
@RestController
@RequestMapping("api/sys")
public class UserSessionRest {

//    @Autowired
//    UserSessionService userSessionService;


    @Autowired
    WebApplicationContext context;

    @Authenticated
    @RequestMapping(value = "/session", method = RequestMethod.POST)
    public UserWorkspaceDTO getUserSession(HttpServletRequest request) {

        UserSession userSession = context.getBean(UserSession.class);
        UserWorkspaceDTO uw = context.getBean(UserWorkspaceDTO.class);

        return uw;
    }
}
