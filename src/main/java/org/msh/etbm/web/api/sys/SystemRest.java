package org.msh.etbm.web.api.sys;

import org.msh.etbm.services.sys.SystemInfoService;
import org.msh.etbm.services.sys.SystemInformation;
import org.msh.etbm.services.sys.SystemState;
import org.msh.etbm.services.usersession.UserSessionService;
import org.msh.etbm.web.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRest.class);

    public final static String PARAM_TYPE = "list";
    public final static String PARAM_TYPE_VALUE = "1";

    @Autowired
    SystemInfoService systemInfoService;

    @Autowired
    UserSessionService userSessionService;


    /**
     * Return information about the system
     * @return instance of SystemInformation
     */
    @RequestMapping("/info")
    public SystemInformation getInformation(@RequestHeader(value = Constants.AUTH_TOKEN_HEADERNAME, required = false) String authToken,
                                            @RequestParam(value = PARAM_TYPE, required = false) String plist) {
        boolean paramList = PARAM_TYPE_VALUE.equals(plist);

        SystemInformation inf = systemInfoService.getInformation(paramList);

        // check if system is ready
        if (inf.getState() == SystemState.READY) {

            // convert authentication token to UUID
            UUID uuidToken;
            try {
                uuidToken = authToken != null ? UUID.fromString(authToken) : null;
            } catch (IllegalArgumentException e) {
                LOGGER.error("Invalid token UUID format : " + authToken, e);
                uuidToken = null;
            }

            // check if authentication is required
            boolean authRequired = uuidToken == null ||
                    userSessionService.recoverSession(uuidToken) == null;

            if (authRequired) {
                inf.setState(SystemState.AUTH_REQUIRED);
            }
        }

        return inf;
    }
}
