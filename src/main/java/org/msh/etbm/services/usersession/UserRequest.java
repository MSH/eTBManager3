package org.msh.etbm.services.usersession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * Basic information about the user session
 *
 * Created by rmemoria on 30/9/15.
 */
@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRequest {

    private UserSession userSession;

    /**
     * If true, the current request is being executed under a declared command
     */
    private boolean commandExecuting;


    /**
     * Check if the user was properly authenticated
     * @return true if the user is authenticated
     */
    public boolean isAuthenticated() {
        return userSession != null;
    }


    /**
     * Return true if the given permission is granted to the user
     * @param perm the permission to check
     * @return true if permission is granted
     */
    public boolean isPermissionGranted(String perm) {
        return userSession != null? userSession.isPermissionGranted(perm): false;
    }

    public boolean isCommandExecuting() {
        return commandExecuting;
    }

    public void setCommandExecuting(boolean commandExecuting) {
        this.commandExecuting = commandExecuting;
    }

    public UserSession getUserSession() {
        return userSession;
    }

    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
