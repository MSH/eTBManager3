package org.msh.etbm.commons.indicators;

/**
 * Exceptions thrown by the indicators library
 *
 * Created by rmemoria on 4/10/16.
 */
public class IndicatorException extends RuntimeException {

    public IndicatorException() {
    }

    public IndicatorException(String message) {
        super(message);
    }

    public IndicatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndicatorException(Throwable cause) {
        super(cause);
    }

    public IndicatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
