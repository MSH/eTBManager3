package org.msh.etbm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * Interceptor that gets the locale in the request and set it to the locale resolver
 *
 * Created by rmemoria on 10/11/15.
 */
@Component
public class LocaleRequestInterceptor extends HandlerInterceptorAdapter {

    @Value("${app.default-language}")
    String defaultLanguage;

    @Value("${app.languages}")
    String[] languages;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        Locale locale = getLocale(request);

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

        if (localeResolver == null) {
            throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
        }

        localeResolver.setLocale(request, response, locale);

        return true;
    }


    /**
     * Return the application locale based on the request object.
     * @param request instance of the request made from the client side
     * @return the app locale, compatible with the locale in the request
     */
    public Locale getLocale(HttpServletRequest request) {
        // try to get the locale selected by the user
        Locale locale = getUserSelectedLocale(request);

        // user selected no locale ?
        if (locale == null) {
            locale = request.getLocale();
        }

        return getCompatibleLocale(locale);
    }

    /**
     * Return the closest application locale based on the given locale.
     * @param locale the selected locale of the user
     * @return the compatible locale of the system
     */
    protected Locale getCompatibleLocale(Locale locale) {
        String locCode = locale.toString();

        // search for locales with same language and country
        for (String lang: languages) {
            if (locCode.equals(lang)) {
                return locale;
            }
        }

        // search for locales with same language
        for (String lang: languages) {
            if (lang.startsWith(locale.getLanguage())) {
                return new Locale(lang);
            }
        }

        // return the locale of the default language
        return new Locale(defaultLanguage);
    }

    /**
     * Return the locale selected by the user
     * @param request
     * @return
     */
    protected Locale getUserSelectedLocale(HttpServletRequest request) {
        String newLocale = null;

        // search for selected language in cookies
        Cookie[] cookies = request.getCookies();
        for (Cookie c: cookies) {
            if (c.getName().equals(Constants.LANG_TOKEN_COOKIE)) {
                newLocale = c.getValue();
                break;
            }
        }

        // if language is null, tries to get it from the parameter
        if (newLocale == null) {
            newLocale = request.getParameter(Constants.LANG_TOKEN_PARAM);
        }

        // no locale selected by the user? return null
        if (newLocale == null) {
            return null;
        }

        return StringUtils.parseLocaleString(newLocale);
    }
}
