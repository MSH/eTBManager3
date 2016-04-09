package org.msh.etbm.commons;

/**
 * Static class with common error message keys, in order to make it easier to get the correct
 * message based on the user selected language
 *
 * Created by rmemoria on 10/11/15.
 */
public class ErrorMessages {

    /**
     * A private constructor to avoid creation of this class
     */
    private ErrorMessages() {
        super();
    }

    public static final String REQUIRED = "NotNull";
    public static final String NOT_UNIQUE = "NotUnique";
    public static final String NOT_VALID = "NotValid";
    public static final String NOT_VALID_WORKSPACE = "NotValidWorkspace";
}
