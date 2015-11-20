package org.msh.etbm.web.api.sys;

import org.msh.etbm.services.sys.SystemInfoService;
import org.msh.etbm.services.sys.SystemInformation;
import org.msh.etbm.services.sys.SystemState;
import org.msh.etbm.services.usersession.UserSessionService;
import org.msh.etbm.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST API to handle system requests
 *
 * Created by rmemoria on 16/8/15.
 */
@RestController
@RequestMapping("/api/sys")
public class SystemRest {

    @Autowired
    SystemInfoService systemInfoService;

    @Autowired
    UserSessionService userSessionService;


    /**
     * Return information about the system
     * @return instance of SystemInformation
     */
    @RequestMapping("/info")
    public SystemInformation getInformation(@RequestHeader(value = Constants.AUTH_TOKEN_HEADERNAME, required = false) String authToken) {
        SystemInformation inf = systemInfoService.getInformation();

        // check if system is ready
        if (inf.getState() == SystemState.READY) {

            // convert authentication token to UUID
            UUID uuidToken;
            try {
                uuidToken = authToken != null? UUID.fromString(authToken) : null;
            }
            catch (IllegalArgumentException e) {
                uuidToken = null;
            }

            // check if authentication is required
            boolean authRequired = uuidToken == null ||
                    userSessionService.getSessionByAuthToken(uuidToken) == null;

            if (authRequired) {
                inf.setState(SystemState.AUTH_REQUIRED);
            }
        }

        return inf;
    }
}
