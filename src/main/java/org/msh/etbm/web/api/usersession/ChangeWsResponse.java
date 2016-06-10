package org.msh.etbm.web.api.usersession;

import org.msh.etbm.services.session.usersession.UserSessionResponse;

import java.util.UUID;

/**
 * Response sent to the client when the workspace changes
 * Created by rmemoria on 12/1/16.
 */
public class ChangeWsResponse {
    private UUID authToken;
    private UserSessionResponse session;

    public ChangeWsResponse(UUID authToken, UserSessionResponse session) {
        this.authToken = authToken;
        this.session = session;
    }

    public UUID getAuthToken() {
        return authToken;
    }

    public void setAuthToken(UUID authToken) {
        this.authToken = authToken;
    }

    public UserSessionResponse getSession() {
        return session;
    }

    public void setSession(UserSessionResponse session) {
        this.session = session;
    }
}
