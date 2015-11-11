package org.msh.etbm.web;

import org.msh.etbm.web.api.authentication.AuthenticatorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

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

    @Autowired
    MessageSource messageSource;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // add interceptor to authenticate the user
        registry.addInterceptor(authenticatorInterceptor);

        // add interceptor to set the current language
        LocaleRequestInterceptor loc = new LocaleRequestInterceptor();
        registry.addInterceptor(loc);

        super.addInterceptors(registry);
    }


    /**
     * Language support
     * @param defaultLanguage the default language of the system
     * @return instance of {@link LocaleResolver}
     */
    @Bean
    public LocaleResolver localeResolver(@Value("${app.default-language}") String defaultLanguage) {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        Locale locale = new Locale(defaultLanguage);
        slr.setDefaultLocale(locale);
        return slr;
    }

    /**
     * Interceptor to get the language
     * @return instance of the LocaleChangeInterceptor
     */
//    @Bean
//    public LocaleRequestInterceptor localeChangeInterceptor() {
//        return new LocaleRequestInterceptor();
//    }

    /**
     * Indicate the validator to use the same resource as the app uses
     * @return instance of Validator class
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }
}
