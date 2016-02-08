package org.msh.etbm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Locale;

/**
 * Resolve message keys in the current locale, logging not existing messages
 *
 * Created by rmemoria on 3/2/16.
 */
@Component
public class Messages {

    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    @Resource
    MessageSource messageSource;

    /**
     * Get the message by its key
     * @param key the message key
     * @return the message to be displayed to the user
     */
    public String get(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            return messageSource.getMessage(key, null, locale);
        }
        catch (NoSuchMessageException e) {
            LOGGER.warn("No message found for " + key + " in the locale " + locale.getDisplayName());
            return key;
        }
    }
}
