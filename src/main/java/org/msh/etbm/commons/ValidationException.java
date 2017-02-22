package org.msh.etbm.commons;

/**
 * Exception generated during service execution to indicate that an error occured and operation
 * cannot continue
 * <p>
 * Created by msantos on 18/01/17.
 */
public class ValidationException extends RuntimeException {

    private String code;
    private String message;

    /**
     * Constructor when there is just one single validation error message
     *
     * @param message
     * @param code
     */
    public ValidationException(String message, String code) {
        super(message);

        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }
}
