package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.impl.JsonModelParser2;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by rmemoria on 8/8/16.
 */
public class JsonModelParserTest {

    @Test
    public void parseForm() throws IOException {
        ClassPathResource res = new ClassPathResource("/test/patient-model.json");
        JsonModelParser2 parser = new JsonModelParser2();

        Model model = parser.parse(res.getInputStream());

        assertNotNull(model);
        assertNotNull(model.getFields());
        assertNotNull(model.getValidators());
        assertNotNull(model.getTable());
        assertNotNull(model.getName());

        assertEquals("patient", model.getTable());
        assertEquals("patient", model.getName());

        // check validators
        assertEquals(2, model.getValidators().size());
        assertTrue(model.getValidators().get(0) instanceof Validator);
        assertTrue(model.getValidators().get(1) instanceof Validator);

        // check fields

    }
}
