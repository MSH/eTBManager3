package org.msh.etbm.services.security;

import org.msh.etbm.services.security.UserConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

/**
 * Utility functions to handle common operations with passwords
 *
 * Created by rmemoria on 1/9/15.
 */
public class UserUtils {

    /**
     * Aplica hash MD5 na senha informada
     * @param password
     * @return
     */
    public static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] hashGerado = md.digest();

            StringBuffer ret = new StringBuffer(hashGerado.length);
            for (int i = 0; i < hashGerado.length; i++) {
                String hex = Integer.toHexString(0x0100 + (hashGerado[i] & 0x00FF)).substring(1);
                ret.append((hex.length() < 2 ? "0" : "") + hex);
            }
            return ret.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException( e );
        }
    }


    /**
     * Check if password is following the minimum requirements to be considered a good password
     * @param pwd the password to be tested
     * @return true if the given password is valid
     */
    public static boolean isValidPassword(String pwd) {
        if (pwd == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(UserConstants.PASSWORD_PATTERN);
        return pattern.matcher(pwd).matches();
    }


    /**
     * Check if e-mail address is valid
     * @param email the e-mail address to be validated
     * @return true if e-mail is valid
     */
    public static final boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(UserConstants.EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }

}
