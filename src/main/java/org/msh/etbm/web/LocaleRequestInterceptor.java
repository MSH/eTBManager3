package org.msh.etbm.web;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor that gets the locale in the request and set it to the locale resolver
 *
 * Created by rmemoria on 10/11/15.
 */
public class LocaleRequestInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {
        // get the language token in the request
        String newLocale = request.getHeader(Constants.LANG_TOKEN_HEADERNAME);

        // if language is null, tries to get it from the parameter
        if (newLocale == null) {
            newLocale = request.getParameter(Constants.LANG_TOKEN_PARAM);
        }

        if (newLocale != null) {
            LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
            if (localeResolver == null) {
                throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
            }

            localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
        }

        return true;
    }
}
