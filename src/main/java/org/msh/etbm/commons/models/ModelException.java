package org.msh.etbm.commons.models;

/**
 * Model specific runtime exception
 *
 * Created by rmemoria on 1/7/16.
 */
public class ModelException extends RuntimeException {
    public ModelException() {
    }

    public ModelException(String message) {
        super(message);
    }

    public ModelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }

    public ModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
