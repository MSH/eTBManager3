package org.msh.etbm.web.templates;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Controller to expose the main page of the system (the root page)
 * Created by rmemoria on 26/8/15.
 */
@Controller
public class IndexController {

    @Value("${app.languages}")
    private String[] languages;

    @Value("${app.default-language}")
    private String defaultLanguage;

    @Value("${server.context-path:}")
    private String contextPath;


    /**
     * Fill the page variables and return the name of the template page
     * @param model the injected model
     * @return
     */
    @RequestMapping("/")
    public String welcome(Map<String, Object> model, HttpServletResponse response) {
        model.put("languages", getLanguagesJS());
        model.put("path", contextPath);
        model.put("defaultLanguage", defaultLanguage);

        // avoid page to be included in the browser cache
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        return "index";
    }

    /**
     * Return the list of languages to be injected into JS code
     * @return String value
     */
    protected String getLanguagesJS() {
        // prepare list of languages available
        String s = "";
        for (String lang: languages) {
            if (!s.isEmpty()) {
                s += ",";
            }
            s += lang;
        }
        return s;
    }
}
