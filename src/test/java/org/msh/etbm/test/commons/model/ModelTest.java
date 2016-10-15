package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ValidationResult;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.JSFunction;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.*;
import org.msh.etbm.commons.models.db.SQLGenerator;
import org.msh.etbm.commons.models.db.SQLGeneratorData;
import org.msh.etbm.commons.models.impl.ModelConverter;
import org.msh.etbm.commons.models.impl.ValidationContext;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 1/7/16.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(Application.class)
//@WebAppConfiguration
public class ModelTest {

    private static final String CITY = "Rio de Janeiro";

//    @Test
//    public void checkWriteRead() {
//        Model model = createModel();
//
//        assertEquals(3, model.getFields().size());
//
//        String s = JsonParser.objectToJSONString(model, true);
//
//        System.out.println(s);
//
//        Model m2 = JsonParser.parseString(s, Model.class);
//        assertNotNull(m2);
//        assertEquals(model.getId(), m2.getId());
//        assertEquals(model.getTable(), m2.getTable());
//
//        assertNotNull(m2.getFields());
//        assertEquals(model.getFields().size(), m2.getFields().size());
//        assertEquals(StringField.class, m2.getFields().get(0).getClass());
//    }


    @Test
    public void testConverter() {
        Map<String, Object> data = new HashMap<>();

        data.put("name", "My admi unit");
        data.put("level", 1);

        Model model = createModel();
        CompiledModel compiledModel = new CompiledModel(model);
        ValidationContext context = compiledModel.createContext(data);

        ModelConverter converter = new ModelConverter();
        Map<String, Object> data2 = converter.convert(context);
        // active field was included
        assertEquals(0, context.getErrors().getErrorCount());
        assertEquals(data.size() + 1, data2.size());
        assertNotEquals(data.get("name"), data2.get("name"));
        assertEquals(data.get("name").toString().toUpperCase(), data2.get("name"));
        assertEquals(data.get("level"), data2.get("level"));
        assertEquals(true, data2.get("active"));

        // level now will receive a string
        data.put("level", "5");
        context = compiledModel.createContext(data);
        data2 = converter.convert(context);
        assertEquals(0, context.getErrors().getErrorCount());
        assertEquals(5, data2.get("level"));

        // field doesn't exist, so will be ignored
        data.put("notExist", 11);
        context = compiledModel.createContext(data);
        data2 = converter.convert(context);
        assertEquals(0, context.getErrors().getErrorCount());
        assertEquals(3, data2.keySet().size());


        // invalid value
        data.remove("noExist");
        data.put("active", new Date());
        context = compiledModel.createContext(data);
        data2 = converter.convert(context);
        assertEquals(1, context.getErrors().getErrorCount());
        assertNotNull(data2);
        FieldError err = context.getErrors().getFieldError("active");
        assertNotNull(err);
        assertEquals(Messages.NOT_VALID, err.getCode());

        data.put("city", " " + CITY);
        data.put("active", true);
        context = compiledModel.createContext(data);
        data2 = converter.convert(context);
        assertNotEquals(data2.get("city"), data.get("city"));
        assertEquals(data2.get("city"), data.get("city").toString().trim());

        // check invalid conversion from string to int
        data.put("level", "--");
        context = compiledModel.createContext(data);
        data2 = converter.convert(context);
        assertEquals(1, context.getErrors().getErrorCount());
        assertNotNull(data2);
        err = context.getErrors().getFieldError("level");
        assertNotNull(err);
        assertEquals(Messages.NOT_VALID, err.getCode());

    }


//    @Test
//    public void generateScript() throws Exception {
//        Model model = createModel();
//        ModelScriptGenerator gen = new ModelScriptGenerator();
//        String script = gen.generate(model);
//        System.out.println(script);
//
//        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
//
//        engine.eval(script);
//    }


    @Test
    public void testSQLGenerator() {
        Model model = createModel();

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", "Ricardo");
        doc.put("level", 2);
        doc.put("city", "Rio de Janeiro");

        SQLGenerator gen = new SQLGenerator();
        SQLGeneratorData data = gen.createInsertSQL(model, doc, UUID.randomUUID());
        assertNotNull(data.getSql());
        assertNotNull(data.getParams());
        assertNotNull(data.getParams().get("id"));

        data = gen.createUpdateSQL(model, doc, UUID.randomUUID());
    }

    @Test
    public void testValidation() {
        Model model = createModel();
        CompiledModel compiledModel = new CompiledModel(model);

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", "Ricardo");
        doc.put("level", 2);

        // city is required
        ValidationResult res = compiledModel.validate(doc, null);
        Errors errors = res.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.getErrorCount());
        FieldError err = errors.getFieldError("city");
        assertNotNull(err);
        assertEquals(err.getCode(), Messages.NOT_NULL);

        // include city
        doc.put("city", "Rio de Janeiro");
        res = compiledModel.validate(doc, null);

        errors = res.getErrors();
        assertNotNull(errors);
        assertEquals(0, errors.getErrorCount());

        // include city as a null value (error)
        doc.put("city", null);
        res = compiledModel.validate(doc, null);
        errors = res.getErrors();
        assertEquals(1, errors.getErrorCount());
        err = errors.getFieldError("city");
        assertNotNull(err);
        assertEquals(err.getCode(), Messages.NOT_NULL);

        // reduce level requirement
        doc.put("level", 1);
        doc.remove("city");
        res = compiledModel.validate(doc, null);
        errors = res.getErrors();
        assertNotNull(errors);
        assertEquals(1, errors.getErrorCount());
        ObjectError error = errors.getGlobalErrors().get(0);
        assertNotNull(error.getDefaultMessage());
    }



    protected Model createModel() {
        Model model = new Model();
        model.setName("adminUnit");
        model.setTable("administrativeUnit");

        List<Field> fields = new ArrayList<>();

        // field name
        StringField fldName = new StringField();
        assertNotNull(fldName.getTypeName());

        fldName.setName("name");
        fldName.setCharCase(CharCase.UPPER);
        fldName.setLabel("Admin unit name");
        fldName.setRequired(JSFuncValue.of(true));
        fldName.setMax(150);
        fields.add(fldName);

        // field level
        IntegerField fldLevel = new IntegerField();
        fldLevel.setName("level");
        fields.add(fldLevel);

        // field active
        BoolField fldActive = new BoolField();
        fldActive.setName("active");
        fldActive.setDefaultValue(JSFuncValue.of(true));
        fields.add(fldActive);

        StringField fldCity = new StringField();
        fldCity.setName("city");
        fldCity.setMax(20);
        fldCity.setRequired(JSFuncValue.function("this.level > 1"));
        fldCity.setTrim(true);
        fields.add(fldCity);

        model.setFields(fields);

        List<Validator> validators = new ArrayList<>();
        Validator v = new Validator();
        v.setRule(new JSFunction("this.name == 'RICARDO' ? this.city == 'Rio de Janeiro' : true"));
        v.setMessage("${" + Messages.NOT_VALID_EMAIL + "}");
        validators.add(v);

        model.setValidators(validators);

        return model;
    }
}
