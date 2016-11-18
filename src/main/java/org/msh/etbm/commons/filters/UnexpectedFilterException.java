package org.msh.etbm.commons.filters;

/**
 * Exception thrown when something unexpected happened, like an internal server error
 * Created by rmemoria on 16/11/16.
 */
public class UnexpectedFilterException extends RuntimeException {

    public UnexpectedFilterException() {
        super();
    }

    public UnexpectedFilterException(String message) {
        super(message);
    }

    public UnexpectedFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedFilterException(Throwable cause) {
        super(cause);
    }

    protected UnexpectedFilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
