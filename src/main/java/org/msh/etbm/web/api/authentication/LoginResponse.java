package org.msh.etbm.web.api.authentication;

import java.util.UUID;

/**
 * Standard response of the login API call
 * <p>
 * Created by rmemoria on 29/9/15.
 */
public class LoginResponse {
    /**
     * If true, indicate if the login was successfully done
     */
    private boolean success;

    /**
     * If authenticated, contains the authentication token to be used in future calls that require authentication
     */
    private UUID authToken;

    /**
     * Constructor passing the success and authentication token
     *
     * @param success   indicates if login was succeeded
     * @param authToken the authentication token
     */
    public LoginResponse(boolean success, UUID authToken) {
        this.success = success;
        this.authToken = authToken;
    }

    /**
     * Default constructor
     */
    public LoginResponse() {
        super();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public UUID getAuthToken() {
        return authToken;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }
}
