package org.msh.etbm.commons.objutils;

/**
 * Exception thrown by {@link ObjectUtils} when something goes wrong during object hashing
 * Created by rmemoria on 9/1/17.
 */
public class ObjectHashException extends RuntimeException {
    public ObjectHashException() {
    }

    public ObjectHashException(String message) {
        super(message);
    }

    public ObjectHashException(String message, Throwable cause) {
        super(message, cause);
    }

    public ObjectHashException(Throwable cause) {
        super(cause);
    }

    public ObjectHashException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
