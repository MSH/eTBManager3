package org.msh.etbm.commons.dbcache;

/**
 * Exception thrown by the dbcache package
 *
 * Created by rmemoria on 9/1/17.
 */
public class DbCacheException extends RuntimeException {
    public DbCacheException() {
    }

    public DbCacheException(String message) {
        super(message);
    }

    public DbCacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public DbCacheException(Throwable cause) {
        super(cause);
    }

    public DbCacheException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
