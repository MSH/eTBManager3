package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.forms.controls.Control;
import org.msh.etbm.commons.forms.data.Form;
import org.msh.etbm.commons.forms.data.SingleDataModel;
import org.msh.etbm.commons.forms.impl.JsonFormParser;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.impl.StandardJSONParser;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rmemoria on 6/8/16.
 */
public class JSONParseTest {

    @Test
    public void parseForm() throws IOException {
        ClassPathResource res = new ClassPathResource("/test/forms/parse-test.json");
        JsonFormParser parser = new JsonFormParser();
        Form frm = parser.parse(res.getInputStream());
        assertNotNull(frm);
        assertNotNull(frm.getDataModel());
        assertNotNull(frm.getDefaultProperties());
        assertNotNull(frm.getTitle());
        assertNotNull(frm.getValidators());

        assertEquals(SingleDataModel.class, frm.getDataModel().getClass());
        assertNotNull(((SingleDataModel)frm.getDataModel()).getModelId());

        // test validators
        testValidators(frm);

        // test controls
        testControls(frm);
    }

    private void testValidators(Form frm) {
        assertEquals(1, frm.getValidators().size());
        assertTrue(frm.getValidators().get(0) instanceof Validator);
        Validator v = frm.getValidators().get(0);
        assertNotNull(v.getRule());
        assertNotNull(v.getMessage());

        assertEquals("this.name !== 'Rio'", v.getRule().getExpression());
        assertEquals("name cannot be Rio", v.getMessage());
    }


    private void testControls(Form frm) {
        assertTrue(frm.getControls() instanceof List);
        for (Control ctrl: frm.getControls()) {
            assertNotNull(ctrl.getType());
            assertNotNull(ctrl.getId());
        }
    }
}
