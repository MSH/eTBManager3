package org.msh.etbm.commons;

/**
 * Created by rmemoria on 26/8/16.
 */
public class JsonParser2Exception extends RuntimeException {

    public JsonParser2Exception() {
    }

    public JsonParser2Exception(String message) {
        super(message);
    }

    public JsonParser2Exception(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParser2Exception(Throwable cause) {
        super(cause);
    }

    public JsonParser2Exception(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
