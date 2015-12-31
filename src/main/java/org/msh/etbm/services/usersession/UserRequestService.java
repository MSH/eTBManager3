package org.msh.etbm.services.usersession;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Basic information about the user session
 *
 * Created by rmemoria on 30/9/15.
 */
@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRequestService {

    public static final String KEY_ADMUNITSERIES_LIST = "admunit-series";

    /**
     * The user session assigned to the request
     */
    private UserSession userSession;

    /**
     * The authentication token used by the client to log into the system
     */
    private UUID authToken;

    /**
     * If true, the current request is being executed under a declared command
     */
    private boolean commandExecuting;

    /**
     * Temporary data stored in the request to be used among components
     */
    private Map<String, Object> requestAttribs;

    /**
     * Check if the user was properly authenticated
     * @return true if the user is authenticated
     */
    public boolean isAuthenticated() {
        return userSession != null;
    }

    /**
     * Return a request data previously put in the <code>put</code>
     * @param key the data key
     * @return the data assigned to the key, or null if no data is available
     */
    public Object get(String key) {
        return requestAttribs != null? requestAttribs.get(key) : null;
    }

    /**
     * Set a request data to be used along the request
     * @param key the data key
     * @param data the data to be assigned to the key
     */
    public void put(String key, Object data) {
        if (requestAttribs == null) {
            requestAttribs = new HashMap<>();
        }

        requestAttribs.put(key, data);
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

    public UUID getAuthToken() {
        return authToken;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }
}
