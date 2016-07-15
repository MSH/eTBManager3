package org.msh.etbm.commons.messages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Component to replace the keys separated by {} into messages from the resource files
 * using the current language.
 * <p>
 * It is also the factory for MessageList objects
 * <p>
 * Created by rmemoria on 27/10/15.
 */
@Component
public class MessageKeyResolver {

    /**
     * The pattern to get the keys to be replaced inside the string
     */
    public static final Pattern EXP_PATTERN = Pattern.compile("\\{(.*?)\\}");

    @Autowired
    MessageSource messageSource;

    /**
     * Create an instance of MessageList class with support for key replacement
     *
     * @return
     */
    public MessageList createMessageList() {
        return new MessageList(this);
    }


    /**
     * Evaluate the given text replacing the keys between {} by messages in the resoure files
     *
     * @param text the text to have its key messages replaced
     * @return the text replaced
     */
    public String evaluateExpression(String text) {
        Matcher matcher = EXP_PATTERN.matcher(text);
        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(1, s.length() - 1);

            text = text.replace(s, translate(key));
        }

        return text;
    }

    protected String translate(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource != null ? messageSource.getMessage(key, null, locale) : key;
    }
}
