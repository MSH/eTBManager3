package org.msh.etbm.web.api.authentication;

import org.msh.etbm.services.security.authentication.AvailableWorkspacesService;
import org.msh.etbm.services.security.authentication.LoginService;
import org.msh.etbm.services.security.authentication.WorkspaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Set of REST API for authentication services
 * Created by rmemoria on 21/8/15.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthenticatorREST {

    @Autowired
    LoginService loginService;

    @Autowired
    AvailableWorkspacesService availableWorkspacesService;

    /**
     * API function to log user into the system
     * @param credentials information about user login, password and workspace
     * @param request the HTTP client request information
     * @return Login response indicating if operation was successful or failed
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody @Valid LoginRequest credentials, HttpServletRequest request) {
        String ipAddr = request.getRemoteAddr();
        String app = request.getHeader("User-Agent");

        // authenticate the user
        UUID authToken = loginService.login(credentials.getUsername(),
                credentials.getPassword(),
                credentials.getWorkspaceId(),
                ipAddr,
                app);

        return new LoginResponse(authToken != null, authToken);
    }

    /**
     * API to log user out of the system by its authentication token.
     * @param authToken a valid authentication token
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public void logout(@RequestParam(value = "tk", required = true) UUID authToken) {
        loginService.logout(authToken);
    }


    /**
     * API to return the list of workspaces available for the given user
     * @param req contain the user credential to get information from
     * @return list of workspaces
     */
    @RequestMapping(value = "/workspaces", method = RequestMethod.POST)
    public List<WorkspaceInfo> getWorkspaces(@RequestBody @Valid WorkspacesRequest req) {
        return availableWorkspacesService.getWorkspaces(req.getUsername(), req.getPassword());
    }
}
