package org.msh.etbm.services.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when the client tries to login or uses an invalid login credential
 * to access a protected resource
 *
 * Created by rmemoria on 29/5/15.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructor where a message can be specified
     * @param msg
     */
    public UnauthorizedException(String msg) {
        super(msg);
    }

    /**
     * Default constructor
     */
    public UnauthorizedException() {
        super();
    }
}
