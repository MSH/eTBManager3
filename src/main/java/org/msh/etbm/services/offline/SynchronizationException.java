package org.msh.etbm.services.offline;

/**
 * Default exception thrown by synchronization operations
 *
 * Created by rmemoria on 8/11/16.
 */
public class SynchronizationException extends RuntimeException {

    public SynchronizationException() {
    }

    public SynchronizationException(String message) {
        super(message);
    }

    public SynchronizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SynchronizationException(Throwable cause) {
        super(cause);
    }

    public SynchronizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
