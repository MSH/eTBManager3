package org.msh.etbm.web.templates;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.Map;

/**
 * Controller to expose the main page of the system
 * Created by rmemoria on 26/8/15.
 */
@Controller
public class IndexController {

    @RequestMapping("/index2.html")
    public String welcome(Map<String, Object> model) {
        model.put("time", new Date());
        model.put("message", "Hello world");
        return "index";
    }
}
