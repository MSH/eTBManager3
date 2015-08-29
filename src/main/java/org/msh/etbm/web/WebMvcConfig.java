package org.msh.etbm.web;

import org.msh.etbm.web.api.authentication.AuthenticatorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * Provide configuration of the interceptors used in the web app
 *
 * Created by rmemoria on 21/8/15.
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Autowired
    AuthenticatorInterceptor authenticatorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // add interceptor to authenticate the user
        registry.addInterceptor(authenticatorInterceptor);

        // add interceptor to set the current language
        LocaleChangeInterceptor loc = new LocaleChangeInterceptor();
        loc.setParamName("lang");
        registry.addInterceptor(loc);

        super.addInterceptors(registry);
    }

    /**
     * Language support
     * @param defaultLanguage the default language of the system
     * @return
     */
    @Bean
    public LocaleResolver localeResolver(@Value("${app.default-language}") String defaultLanguage) {
        CookieLocaleResolver res = new CookieLocaleResolver();
        res.setDefaultLocale(new Locale(defaultLanguage));
        res.setCookieName("lang");
        return res;
    }

}
