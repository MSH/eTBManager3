package org.msh.etbm.test.commons.model;

import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.NashornScriptEngine;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Test;
import org.msh.etbm.Messages;
import org.msh.etbm.commons.models.PreparedModel;
import org.msh.etbm.commons.models.data.JSExprValue;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.*;
import org.msh.etbm.commons.models.impl.ModelScriptGenerator;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.script.*;
import javax.validation.Valid;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 1/7/16.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(Application.class)
//@WebAppConfiguration
public class ModelTest {


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


//    @Test
//    public void testValidation() {
//        Map<String, Object> data = new HashMap<>();
//
//        data.put("name", "My admi unit");
//        data.put("level", 1);
//
//        Model model = createModel();
//
//        ModelConverter converter = new ModelConverter();
//        Map<String, Object> data2 = converter.convert(model, data);
//        // active field was included
//        assertEquals(data.size() + 1, data2.size());
//        assertEquals(data.get("name"), data2.get("name"));
//        assertEquals(data.get("level"), data2.get("level"));
//
//        // level now will receive a string
//        data.put("level", "5");
//        data2 = converter.convert(model, data);
//
//        assertEquals(5, data2.get("level"));
//    }


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
    public void testValidation() {
        Model model = createModel();
        PreparedModel preparedModel = new PreparedModel(model);

        Map<String, Object> doc = new HashMap<>();
        doc.put("name", "Ricardo");
        doc.put("level", 2);

        // city is required
        Errors errors = preparedModel.validate(doc);
        assertNotNull(errors);
        assertEquals(1, errors.getErrorCount());
        FieldError err = errors.getFieldError("city");
        assertNotNull(err);
        assertEquals(err.getCode(), Messages.NOT_NULL);

        // include city
        doc.put("city", "Rio de Janeiro");
        errors = preparedModel.validate(doc);

        assertNotNull(errors);
        assertEquals(0, errors.getErrorCount());

        // reduce level requirement
        doc.put("level", 1);
        doc.remove("city");
        errors = preparedModel.validate(doc);
        assertNotNull(errors);
        assertEquals(0, errors.getErrorCount());
    }


//    @Test
//    public void testScript() throws Exception {
//        // generate the script
//        Model model = createModel();
//
//        ModelScriptGenerator gen = new ModelScriptGenerator();
//        String script = gen.generate(model);
//        System.out.println(script);
//
//        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
//
//        // compile the script
//        engine.eval(script);
//
//        Invocable invokable = (Invocable)engine;
//        String funcName = ModelScriptGenerator.modelFunctionName(model);
//        JSObject obj = (JSObject)invokable.invokeFunction(funcName);
//        obj = (JSObject)obj.getMember("fields");
//        obj = (JSObject)obj.getMember("active");
//        obj = (JSObject)obj.getMember("required");
//        assertTrue(obj.isFunction());
//
//        final JSObject func = obj;
//
//        SimpleBindings doc = new SimpleBindings();
//        doc.put("level", 0);
//        Object res = func.call(doc);
//        assertEquals(false, res);
//
//        Callable<Boolean> scriptCall = new Callable<Boolean>() {
//            @Override
//            public Boolean call() throws Exception {
//
//                SimpleBindings doc = new SimpleBindings();
//                doc.put("level", 10);
//
//                return (Boolean)func.call(doc);
//            }
//        };
//
//        ExecutorService executor = Executors.newCachedThreadPool();
//        ArrayList<Future<Boolean>> results = new ArrayList<>();
//
//        for (int i = 0; i < 50; i++) {
//            results.add(executor.submit(scriptCall));
//        }
//
//        int count = 0;
//        for (Future<Boolean> result: results) {
//            Boolean val = result.get().booleanValue();
//            if (val) {
//                count++;
//            }
//        }
//
//        executor.awaitTermination(1, TimeUnit.SECONDS);
//        executor.shutdown();
//
//        System.out.println("Errors = " + count);
//    }


    protected Model createModel() {
        Model model = new Model();
        model.setId("adminUnit");
        model.setTable("administrativeUnit");

        List<Field> fields = new ArrayList<>();

        // field name
        StringField fldName = new StringField();
        assertNotNull(fldName.getTypeName());

        fldName.setName("name");
        fldName.setCharCase(CharCase.UPPER);
        fldName.setLabel("Admin unit name");
        fldName.setRequired(JSExprValue.of(true));
        fldName.setMax(150);
        fields.add(fldName);

        // field level
        IntegerField fldLevel = new IntegerField();
        fldLevel.setName("level");
        fields.add(fldLevel);

        // field active
        BoolField fldActive = new BoolField();
        fldActive.setName("active");
        fldActive.setDefaultValue(true);
        fields.add(fldActive);

        StringField fldCity = new StringField();
        fldCity.setName("city");
        fldCity.setMax(20);
        fldCity.setRequired(JSExprValue.exp("this.level > 1"));
        fields.add(fldCity);

        model.setFields(fields);

        List<Validator> validators = new ArrayList<>();
        Validator v = new Validator();
        v.setJsExpression("this.name == 'Ricardo' ? this.city == 'Rio de Janeiro' : true");
        validators.add(v);

        model.setValidators(validators);

        return model;
    }
}
