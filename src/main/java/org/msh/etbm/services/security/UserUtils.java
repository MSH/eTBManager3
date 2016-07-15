package org.msh.etbm.services.security;

import com.fasterxml.uuid.Generators;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility functions to handle common operations with passwords
 * <p>
 * Created by rmemoria on 1/9/15.
 */
public class UserUtils {

    public static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String PASSWORD_PATTERN = "/((?=.*\\d)(?=.*[a-zA-Z]).{6,20})/";

    // keep it compiled
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * Aplica hash MD5 na senha informada
     *
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
            throw new RuntimeException(e);
        }
    }


    /**
     * Generate a randomly new password
     *
     * @return the password
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
     * Check if password is following the minimum requirements to be considered a good password
     *
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
     *
     * @param email the e-mail address to be validated
     * @return true if e-mail is valid
     */
    public static final boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(UserConstants.EMAIL_PATTERN);
        return pattern.matcher(email).matches();
    }


    /**
     * Generate a new unique token to be used to reset password when user
     * must provide a new password
     *
     * @return unique character sequence
     */
    public static final String generatePasswordToken() {
        UUID id = Generators.timeBasedGenerator().generate();
        String val = id.toString().replace("-", "");
        return val;
    }

}
