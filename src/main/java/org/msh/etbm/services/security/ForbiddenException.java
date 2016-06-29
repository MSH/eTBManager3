package org.msh.etbm.services.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the client tries to login or uses an invalid login credential
 * to access a protected resource
 * <p>
 * Created by rmemoria on 29/5/15.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ForbiddenException extends RuntimeException {

    /**
     * Constructor where a message can be specified
     *
     * @param msg
     */
    public ForbiddenException(String msg) {
        super(msg);
    }

    /**
     * Default constructor
     */
    public ForbiddenException() {
        super();
    }
}
