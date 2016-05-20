package org.msh.etbm.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.web.LocaleRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Created by rmemoria on 13/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class LocaleRequestInterceptorTest {

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    LocaleRequestInterceptor localeRequestInterceptor;

    @Value("${app.default-language}")
    String defaultLanguage;

    @Test
    public void testLocaleInterceptor() throws ServletException {
        assertLocale("en", "en");
        assertLocale("en_US", "en");
        assertLocale("en_CA", "en");

        assertLocale("pt", "pt_BR");
        assertLocale("pt_BR", "pt_BR");
        assertLocale("pt_PT", "pt_BR");

        assertLocale("ru", "ru");
        assertLocale("ru_RU", "ru");
        assertLocale("uk", "uk");
        assertLocale("uk_UK", "uk");

        assertLocale("de", defaultLanguage);
        assertLocale("fr", defaultLanguage);
        assertLocale("fr_CA", defaultLanguage);
    }

    protected void assertLocale(String sourceLocale, String targetLocale) {
        Cookie c = new Cookie("lang", sourceLocale);
        request.setCookies(c);
        Locale locale = localeRequestInterceptor.getLocale(request);

        assertEquals(targetLocale.toLowerCase(), locale.toString().toLowerCase());
    }
}
