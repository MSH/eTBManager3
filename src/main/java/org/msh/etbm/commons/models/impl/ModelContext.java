package org.msh.etbm.commons.models.impl;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;

import javax.script.SimpleBindings;
import java.util.Map;

/**
 * Created by rmemoria on 5/7/16.
 */
public class ModelContext {
    private Model model;
    private ScriptObjectMirror jsModel;
    private Errors errors;
    private Map<String, Object> doc;
    private SimpleBindings docBinding;

    public ModelContext(Model model, ScriptObjectMirror jsModel, Map<String, Object> doc) {
        this.model = model;
        this.jsModel = jsModel;
        this.doc = doc;
        this.errors = new MapBindingResult(doc, model.getName());
    }

    /**
     * Create a context for the field of the model
     * @param field instance of {@link Field} in the model
     * @return instance of {@link FieldContext}
     */
    public FieldContext createFieldContext(Field field) {
        ScriptObjectMirror fields = (ScriptObjectMirror)jsModel.get("fields");
        ScriptObjectMirror jsField = (ScriptObjectMirror)fields.get(field.getName());
        FieldContext fieldContext = new FieldContext(this, field, jsField);

        return fieldContext;
    }

    /**
     * Get the document binding representing the document, to be used in JS engine
     * @return
     */
    public SimpleBindings getDocBinding() {
        if (docBinding == null) {
            docBinding = new SimpleBindings();
            docBinding.putAll(doc);
        }

        return docBinding;
    }

    public Model getModel() {
        return model;
    }

    public ScriptObjectMirror getJsField() {
        return jsModel;
    }

    public Errors getErrors() {
        return errors;
    }

    public Map<String, Object> getDoc() {
        return doc;
    }
}
