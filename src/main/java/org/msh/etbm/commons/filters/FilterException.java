package org.msh.etbm.commons.filters;

/**
 * Exception throw by an instance of {@link Filter}
 * Created by rmemoria on 21/9/16.
 */
public class FilterException extends RuntimeException {
    public FilterException() {
    }

    public FilterException(String message) {
        super(message);
    }

    public FilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterException(Throwable cause) {
        super(cause);
    }

    public FilterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
