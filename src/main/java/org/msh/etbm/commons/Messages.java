package org.msh.etbm.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolve message keys in the current locale, logging not existing messages
 * <p>
 * Created by rmemoria on 3/2/16.
 */
@Component
public class Messages {

    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    public static final String NOT_UNIQUE = "NotUnique";
    public static final String REQUIRED = "NotNull";
    public static final String NOT_NULL = "NotNull";
    public static final String NOT_VALID = "NotValid";
    public static final String NOT_VALID_EMAIL = "NotValidEmail";
    public static final String NOT_VALID_WORKSPACE = "NotValidWorkspace";
    public static final String NOT_UNIQUE_USER = "NotUniqueUser";
    public static final String NOT_VALID_OPTION = "NotValidOption";

    public static final String MAX_SIZE = "javax.validation.constraints.Max.message";
    public static final String MIN_SIZE = "javax.validation.constraints.Min.message";

    /**
     * The pattern to get the keys to be replaced inside the string
     */
    public static final Pattern EXP_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");


    @Resource
    MessageSource messageSource;

    /**
     * Get the message by its key
     *
     * @param key the message key
     * @return the message to be displayed to the user
     */
    public String get(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.info("No message found for " + key + " in the locale " + locale.getDisplayName());
            return key;
        }
    }

    /**
     * Get the message using a message source resolvable object
     *
     * @param res instance of the MessageSourceResolvable interface containing the message
     * @return the string message
     */
    public String get(MessageSourceResolvable res) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(res, locale);
        } catch (NoSuchMessageException e) {
            LOGGER.info("No message found for " + res.getDefaultMessage() + " in the locale " + locale.getDisplayName());
        }
        return res.getDefaultMessage();
    }

    /**
     * Evaluate a string and replace message keys between ${key} by the message in the resource bundle file
     * @param text the string to be evaluated
     * @return the new evaluated string
     */
    public String eval(String text) {
        Matcher matcher = EXP_PATTERN.matcher(text);
        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(2, s.length() - 1);

            text = text.replace(s, get(key));
        }

        return text;
    }
}
