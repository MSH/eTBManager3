package org.msh.etbm.commons;

/**
 * Created by rmemoria on 26/8/16.
 */
public class JsonParserException extends RuntimeException {

    public JsonParserException() {
    }

    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonParserException(Throwable cause) {
        super(cause);
    }

    public JsonParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
