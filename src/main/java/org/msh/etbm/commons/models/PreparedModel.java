package org.msh.etbm.commons.models;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.impl.ModelConverter;
import org.msh.etbm.commons.models.impl.ModelScriptGenerator;
import org.msh.etbm.commons.models.impl.ModelValidator;
import org.springframework.validation.Errors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;

/**
 * Created by rmemoria on 6/7/16.
 */
public class PreparedModel {

    private Model model;
    private ScriptObjectMirror jsModel;

    public PreparedModel(Model model) {
        this.model = model;
    }

    public Errors validate(Map<String, Object> doc) {
        JSObject js = getJsModel();

        // convert the values to the final value
        ModelConverter converter = new ModelConverter();
        Map<String, Object> newdoc = converter.convert(model, doc);

        // call validation
        ModelValidator validator = new ModelValidator();
        return validator.validate(model, jsModel, newdoc);
    }

    public JSObject getJsModel() {
        if (jsModel == null) {
            generateJsModel();
        }

        return jsModel;
    }

    protected void generateJsModel() {
        ModelScriptGenerator gen = new ModelScriptGenerator();
        String script = gen.generate(model);

        ScriptObjectMirror res = compileScript(script);
        jsModel = res;
    }

    private ScriptObjectMirror compileScript(String script) {
        try {
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");

            // compile the script
            engine.eval(script);

            Invocable invokable = (Invocable)engine;
            String funcName = ModelScriptGenerator.modelFunctionName(model);
            return (ScriptObjectMirror)invokable.invokeFunction(funcName);
        } catch (Exception e) {
            throw new ModelException(e);
        }
    }
}
