package org.msh.etbm.test.commons.forms;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.impl.FormParser;
import org.msh.etbm.commons.forms.impl.JavaScriptFormGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.Assert.assertNotNull;

/**
 * Test the generation of java script code to be sent to the client side
 *
 * Created by rmemoria on 26/7/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class JavaScriptFormGenTest {

    @Autowired
    JavaScriptFormGenerator javaScriptFormGenerator;

    @Test
    public void test() throws Exception {
        ClassPathResource resource = new ClassPathResource("/test/forms/parse-test.json");
        FormParser p = new FormParser();
        Form frm = p.parse(resource.getInputStream());

        String script = javaScriptFormGenerator.generate(frm, "newSchema");

        // System.out.println(script);

        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");

        // compile the script to test if it was generated correctly
        engine.eval(script);

        Invocable invokable = (Invocable)engine;
        ScriptObjectMirror res = (ScriptObjectMirror)invokable.invokeFunction("newSchema");
        assertNotNull(res.getMember("defaultProperties"));
        assertNotNull(res.getMember("controls"));
    }
}
