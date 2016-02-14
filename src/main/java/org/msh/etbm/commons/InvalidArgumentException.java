package org.msh.etbm.commons;

/**
 * Custom exception of the application when an invalid argument is used in a service
 *
 * Created by rmemoria on 8/2/16.
 */
public class InvalidArgumentException extends RuntimeException {
    private final String property;
    private final String code;

    /**
     * Default constructor
     * @param property invalid property name
     * @param message message to the user
     * @param code message error code, if available
     */
    public InvalidArgumentException(String property, String message, String code) {
        super(message);
        this.property = property;
        this.code = code;
    }

    /**
     * The invalid property name
     * @return string value
     */
    public String getProperty() {
        return property;
    }

    /**
     * The error code, if available
     * @return string value
     */
    public String getCode() {
        return code;
    }
}
