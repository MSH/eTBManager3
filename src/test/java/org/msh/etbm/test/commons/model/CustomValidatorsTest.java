package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ValidationResult;
import org.msh.etbm.commons.models.data.JSFunction;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.IntegerField;
import org.msh.etbm.commons.models.impl.ModelScriptGenerator;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rmemoria on 6/7/16.
 */
public class CustomValidatorsTest {

    private static final String ERR1 = "Invalid value 1";
    private static final String ERR2 = "Value bigger than 10";

    @Test
    public void testCustomValidators() {
        Model model = createModel(true);

        CompiledModel compiledModel = new CompiledModel(model);

        Map<String, Object> doc = new HashMap<>();
        doc.put("value", 1);

        ValidationResult res = compiledModel.validate(doc, null);
        assertNotNull(res);
        assertNotNull(res.getErrors());

        Errors errors = res.getErrors();
        assertEquals(1, errors.getErrorCount());

        FieldError err = errors.getFieldErrors().get(0);
        assertEquals("value", err.getField());
        assertEquals(ERR1, err.getDefaultMessage());

        // now active the 2nd validator
        doc.put("value", 10);
        res = compiledModel.validate(doc, null);
        assertNotNull(res.getErrors());

        errors = res.getErrors();
        assertEquals(1, errors.getErrorCount());
        err = errors.getFieldErrors().get(0);
        assertEquals("value", err.getField());
        assertEquals(ERR2, err.getDefaultMessage());

        // no error (valid value)
        doc.put("value", 5);
        res = compiledModel.validate(doc, null);
        errors = res.getErrors();
        assertEquals(0, errors.getErrorCount());
    }

    @Test
    public void globalValidatorsTest() {
        Model model = createModel(false);

        CompiledModel compiledModel = new CompiledModel(model);

        // create the document
        Map<String, Object> doc = new HashMap<>();
        doc.put("value", 1);

        ValidationResult res = compiledModel.validate(doc, null);
        assertNotNull(res);
        assertNotNull(res.getErrors());

        Errors errors = res.getErrors();
        assertEquals(1, errors.getErrorCount());

        ObjectError err = errors.getGlobalErrors().get(0);
        assertEquals(ERR1, err.getDefaultMessage());

        // now active the 2nd validator
        doc.put("value", 10);
        res = compiledModel.validate(doc, null);
        assertNotNull(res.getErrors());

        errors = res.getErrors();
        assertEquals(1, errors.getErrorCount());
        err = errors.getGlobalErrors().get(0);
        assertEquals(ERR2, err.getDefaultMessage());

        // no error (valid value)
        doc.put("value", 5);
        res = compiledModel.validate(doc, null);
        errors = res.getErrors();
        assertEquals(0, errors.getErrorCount());
    }

    /**
     * Create a model for testing
     * @param local if true, custom validators will be assigned to a field, otherwise, to the model
     * @return instance of {@link Model}
     */
    public Model createModel(boolean local) {
        Model model = new Model();
        model.setName("test");

        IntegerField fldValue = new IntegerField();
        fldValue.setName("value");

        List<Validator> validators = new ArrayList<>();
        Validator validator = new Validator();
        validator.setRule(new JSFunction("this.value !== 1"));
        validator.setMessage(ERR1);
        validators.add(validator);

        validator = new Validator();
        validator.setRule(new JSFunction("this.value < 10"));
        validator.setMessage(ERR2);
        validators.add(validator);

        if (local) {
            fldValue.setValidators(validators);
        } else {
            model.setValidators(validators);
        }

        List<Field> fields = new ArrayList<>();
        fields.add(fldValue);

        model.setFields(fields);

        ModelScriptGenerator gen = new ModelScriptGenerator();
        String src = gen.generate(model);
        System.out.println(src);

        return model;
    }
}
