package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.CompiledModel;
import org.msh.etbm.commons.models.ValidationResult;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.fields.IntegerField;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.options.FieldRangeOptions;
import org.springframework.validation.FieldError;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the options of a field
 * Created by rmemoria on 8/7/16.
 */
public class FieldOptionsTest {

    @Test
    public void testValidOptionValues() {
        Model model = createModel();
        CompiledModel pm = new CompiledModel(model);

        Map<String, Object> doc = new HashMap<>();
        doc.put("status", "REQUIRED");

        for (int i = 0; i <= 20; i++) {
            doc.put("afb", i);

            // Initial test with no error
            ValidationResult res = pm.validate(doc, null);
            assertNotNull(res);
            assertNotNull(res.getErrors());

            int errorCount = i >= 2 && i <= 10 ? 0 : 1;
            assertEquals(errorCount, res.getErrors().getErrorCount());
        }

        // invalid option in range
        doc.put("afb", 100);
        ValidationResult res = pm.validate(doc, null);
        assertEquals(1, res.getErrors().getErrorCount());
        FieldError err = res.getErrors().getFieldError("afb");
        assertNotNull(err);
        assertEquals(Messages.NOT_VALID_OPTION, err.getCode());

        doc.put("afb", 1);
        res = pm.validate(doc, null);
        assertEquals(1, res.getErrors().getErrorCount());
        err = res.getErrors().getFieldError("afb");
        assertNotNull(err);
        assertEquals(Messages.NOT_VALID_OPTION, err.getCode());

        // invalid option in list
        doc.put("status", "INVALID STATUS");
        doc.put("afb", 5);
        res = pm.validate(doc, null);
        assertEquals(1, res.getErrors().getErrorCount());
        err = res.getErrors().getFieldError("status");
        assertNotNull(err);
        assertEquals(Messages.NOT_VALID_OPTION, err.getCode());
    }

    @Test
    public void testOptions() {
        Model model = createModel();

        // check range options
        Field field = model.findFieldByName("afb");
        assertNotNull(field.getOptions());
        List<Item> options = field.getOptions().getOptionsValues();
        assertNotNull(options);
        assertEquals(9, options.size());
        int index = 2;
        for (Item item: options) {
            assertEquals(index, item.getId());
            index++;
        }

        // check list options
        field = model.findFieldByName("status");
        assertNotNull(field.getOptions());
        options = field.getOptions().getOptionsValues();
        assertNotNull(options);
        assertEquals(3, options.size());
    }

    /**
     * Create a model for testing
     * @return
     */
    protected Model createModel() {
        Model model = new Model();
        model.setName("exam");
        model.setTable("exam");

        List<Field> fields = new ArrayList<>();

        IntegerField fldAFB = new IntegerField();
        fldAFB.setName("afb");
        FieldRangeOptions rangeOpts = new FieldRangeOptions();
        rangeOpts.setIni(2);
        rangeOpts.setEnd(10);
        fldAFB.setOptions(rangeOpts);
        fields.add(fldAFB);

        StringField fldStatus = new StringField();
        fldStatus.setName("status");
        FieldListOptions options = new FieldListOptions();
        List<Item> lst = Arrays.asList(
                new Item("REQUIRED", "Exam required"),
                new Item("ONGOING", "On going exam"),
                new Item("RELEASED", "Results released"));
        options.setList(lst);
        fldStatus.setOptions(options);
        fields.add(fldStatus);

        model.setFields(fields);

        return model;
    }
}
