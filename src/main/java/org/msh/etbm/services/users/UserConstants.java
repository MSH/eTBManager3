package org.msh.etbm.services.users;

/**
 * Constants used for user handling
 *
 * Created by rmemoria on 1/9/15.
 */
public class UserConstants {

    /**
     * Pattern to validate an e-mail address
     */
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * Pattern to validate a password using the password rules (min 6 chars, max 20 chars, one digit and one char at least
     */
    public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-zA-Z]).{6,20})";
}
