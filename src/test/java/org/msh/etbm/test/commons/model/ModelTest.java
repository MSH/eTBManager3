package org.msh.etbm.test.commons.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.models.data.JSExpressionProperty;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.*;
import org.msh.etbm.commons.models.impl.ModelConverter;
import org.msh.etbm.commons.models.impl.ModelValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Created by rmemoria on 1/7/16.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(Application.class)
//@WebAppConfiguration
public class ModelTest {


    @Test
    public void checkWriteRead() {
        Model model = createModel();

        assertEquals(3, model.getFields().size());

        String s = JsonParser.objectToJSONString(model, true);

        System.out.println(s);

        Model m2 = JsonParser.parseString(s, Model.class);
        assertNotNull(m2);
        assertEquals(model.getId(), m2.getId());
        assertEquals(model.getTable(), m2.getTable());

        assertNotNull(m2.getFields());
        assertEquals(model.getFields().size(), m2.getFields().size());
        assertEquals(StringField.class, m2.getFields().get(0).getClass());
    }


    @Test
    public void testValidation() {
        Map<String, Object> data = new HashMap<>();

        data.put("name", "My admi unit");
        data.put("level", 1);

        Model model = createModel();

        ModelConverter converter = new ModelConverter();
        Map<String, Object> data2 = converter.convert(model, data);
        // active field was included
        assertEquals(data.size() + 1, data2.size());
        assertEquals(data.get("name"), data2.get("name"));
        assertEquals(data.get("level"), data2.get("level"));

        // level now will receive a string
        data.put("level", "5");
        data2 = converter.convert(model, data);

        assertEquals(5, data2.get("level"));
    }


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
        fldName.setRequired(new JSExpressionProperty(true));
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

        model.setFields(fields);
        return model;
    }

}
