package org.msh.etbm.web.api.sys;

import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.db.enums.MedicineLine;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Provide global list options used throughout the system. The main reason
 * of this service is to expose all standard lists to the client side
 *
 * Created by rmemoria on 10/12/15.
 */
@Service
public class GlobalListsService {

    @Resource
    MessageSource messageSource;

    static final public Class[] lists = {
            MedicineLine.class,
            CaseClassification.class,
            CaseState.class
    };

    /**
     * Return the options of all lists supported by the system
     * @return
     */
    public Map<String, Map<String, String> > getLists() {
        Map<String, Map<String, String>> res = new HashMap<>();

        Locale locale = LocaleContextHolder.getLocale();

        for (Class clazz: lists) {
            String name = clazz.getSimpleName();
            Map<String, String> options = getOptions(clazz, locale);

            res.put(name, options);
        }

        return res;
    }

    /**
     * Return the options from the given class (actually, just enums are supported)
     * @param clazz the enum class to get options from
     * @param locale the locale to get translated enum values
     * @return the list of options from the enumeration
     */
    private Map<String, String> getOptions(Class clazz, Locale locale) {
        if (!clazz.isEnum()) {
            throw new RuntimeException("Type not supported: " + clazz);
        }

        Object[] vals = clazz.getEnumConstants();

        Map<String, String> opts = new HashMap<>();

        for (Object val: vals) {
            String messageKey = clazz.getSimpleName() + '.' + val.toString();
            String message;
            try {
                message = messageSource.getMessage(messageKey, null, locale);
            }
            catch (NoSuchMessageException e) {
                message = messageKey;
            }

            opts.put(val.toString(), message);
        }

        return opts;
    }

}
