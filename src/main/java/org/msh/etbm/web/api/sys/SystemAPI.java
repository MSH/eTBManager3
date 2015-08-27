package org.msh.etbm.web.api.sys;

import org.msh.etbm.web.api.authentication.AuthConstants;
import org.msh.etbm.services.authentication.AuthenticationService;
import org.msh.etbm.services.sys.SystemInformation;
import org.msh.etbm.services.sys.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST API to handle system requests
 *
 * Created by rmemoria on 16/8/15.
 */
@RestController
@RequestMapping("/api/sys")
public class SystemAPI {

    @Autowired
    SystemService systemService;

    @Autowired
    AuthenticationService authenticationService;

    /**
     * Return information about the system
     * @return instance of SystemInformation
     */
    @RequestMapping("/info")
    public SystemInformation getInformation(@RequestHeader(value = AuthConstants.AUTH_TOKEN_HEADERNAME, required = false) String authToken) {
        SystemInformation inf = systemService.getInformation();

        // check if system is ready
        if (inf.getState() == SystemInformation.SystemState.READY) {
            // check if authentication is required
            boolean authRequired = authToken == null ||
                    authenticationService.getSessionByToken(authToken) == null;

            if (authRequired) {
                inf.setState(SystemInformation.SystemState.AUTH_REQUIRED);
            }
        }

        return inf;
    }
}
