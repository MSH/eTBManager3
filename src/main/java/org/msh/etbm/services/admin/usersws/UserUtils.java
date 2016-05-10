package org.msh.etbm.services.admin.usersws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Set of utilities to support users
 */
public class UserUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUtils.class);

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String PASSWORD_PATTERN = "/((?=.*\\d)(?=.*[a-zA-Z]).{6,20})/";

    // keep it compiled
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * Hash password using the MD5 algorithm
     * @param password the password to be hashed
     * @return the hash in MD5 algorithm
     */
    public static final String hashPassword(String password) {
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
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Gera uma nova senha
     * @return
     */
    public static final String generateNewPassword() {
        String sen = "";
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";

        Random gen = new Random();
        for (int i = 0; i < 6; i++) {
            int index = gen.nextInt(chars.length());
            sen = sen.concat(chars.substring(index, index + 1));
        }

        return sen;
    }


    /**
     * Check if e-mail address is valid
     * @param email the e-mail address to be validated
     * @return true if e-mail is valid
     */
    public static final boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    /**
     * Check if given password is a valid one
     * @param password the password to check
     * @return true if it is a valid password
     */
    public static final boolean isValidPassword(String password) {
        return passwordPattern.matcher(password).matches();
    }
}
