package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.models.PreparedModel;
import org.msh.etbm.commons.models.ValidationResult;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by rmemoria on 8/7/16.
 */
public class StringFieldTest {

    @Test
    public void testStringField() {
        Model model = new Model();
        model.setName("test");

        List<Field> fields = new ArrayList<>();

        StringField fld = new StringField();
        fld.setName("name");
        fld.setMax(10);
        fld.setMin(2);

        fields.add(fld);

        model.setFields(fields);

        PreparedModel pm = new PreparedModel(model);

        // create testing document
        Map<String, Object> doc = new HashMap<>();

        // valid value
        doc.put("name", "Test");
        ValidationResult res = pm.validate(doc);
        assertNotNull(res.getErrors());
        assertEquals(0, res.getErrors().getErrorCount());

        // invalid name (max size)
        doc.put("name", "This is a name longer than allowed");
        res = pm.validate(doc);
        assertEquals(1, res.getErrors().getErrorCount());
        FieldError err = res.getErrors().getFieldError("name");
        assertNotNull(err);
    }
}
