package org.msh.etbm.commons.models;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.impl.ModelContext;
import org.msh.etbm.commons.models.impl.ModelConverter;
import org.msh.etbm.commons.models.impl.ModelScriptGenerator;
import org.msh.etbm.commons.models.impl.ModelValidator;
import org.springframework.validation.Errors;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;

/**
 * {@link PreparedModel} is a class that contains both a {@link Model} and its compiled
 * JavaScript code. The JavaScript code is compiled and used during validation of a document
 * (conversion and validation phases).
 *
 * Created by rmemoria on 6/7/16.
 */
public class PreparedModel {

    private Model model;
    private ScriptObjectMirror jsModel;

    public PreparedModel(Model model) {
        this.model = model;
    }

    /**
     * Validate the given document against the model
     * @param doc the document to check
     * @return
     */
    public ValidationResult validate(Map<String, Object> doc) {
        ModelContext context = createContext(doc);

        // convert the values to the final value
        ModelConverter converter = new ModelConverter();
        Map<String, Object> newdoc = converter.convert(context);

        // check if there are conversion errors
        if (context.getErrors().getErrorCount() > 0) {
            return new ValidationResult(context.getErrors(), newdoc);
        }

        // call validation
        ModelValidator validator = new ModelValidator();
        context = createContext(doc);
        Errors errors = validator.validate(context, newdoc);

        return new ValidationResult(errors, newdoc);
    }

    public ScriptObjectMirror getJsModel() {
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

    public ModelContext createContext(Map<String, Object> doc) {
        return new ModelContext(model, getJsModel(), doc);
    }
}
