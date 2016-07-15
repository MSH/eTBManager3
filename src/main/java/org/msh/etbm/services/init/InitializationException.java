package org.msh.etbm.services.init;

/**
 * Exception thrown during initialization of the system under certain conditions
 * <p>
 * Created by rmemoria on 1/9/15.
 */
public class InitializationException extends RuntimeException {

    public InitializationException() {
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

    public InitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
