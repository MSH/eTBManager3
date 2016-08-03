package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.JsonParser;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.data.options.FieldListOptions;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test parsing and serialization of a model to JSON
 * Created by rmemoria on 7/7/16.
 */
public class ModelReadWriteTest {

    @Test
    public void writeAndRead() {
        Model model = createModel();
        String s = JsonParser.objectToJSONString(model, true);

        System.out.println(s);

        Model m2 = JsonParser.parseString(s, Model.class);
        assertNotNull(m2);
        assertNotNull(m2.getFields());
        assertEquals(model.getFields().size(), m2.getFields().size());

        Field f = model.findFieldByName("status");
        assertNotNull(f);
        assertNotNull(f.getOptions());
    }

    protected Model createModel() {
        Model model = new Model();
        model.setName("tbcase");
        model.setTable("tbcase");

        List<Field> fields = new ArrayList<>();

        // field name
        StringField fldName = new StringField();

        fldName.setName("name");
        fldName.setCharCase(CharCase.UPPER);
        fldName.setLabel("Patient name");
        fldName.setRequired(JSFuncValue.of(true));
        fldName.setMax(150);
        fields.add(fldName);

        // field level
        IntegerField fldAge = new IntegerField();
        fldAge.setName("age");
        fields.add(fldAge);

        StringField fldStatus = new StringField();
        fldStatus.setName("status");
        FieldListOptions options = new FieldListOptions();
        List<Item> lst = Arrays.asList(
                new Item("NOT_ON_TREATMENT", "Not on treatment"),
                new Item("ON_TREATMENT", "On treatment"),
                new Item("CLOSED", "Closed"));
        options.setList(lst);
        fldStatus.setOptions(options);
        fields.add(fldStatus);

        FKRegimenField fldRegimen = new FKRegimenField();
        fldRegimen.setName("regimen_id");
        fields.add(fldRegimen);

        model.setFields(fields);

        List<Validator> validators = new ArrayList<>();
        Validator v = new Validator();
        v.setRule("this.name == 'Ricardo' ? this.city == 'Rio de Janeiro' : true");
        v.setMessage("${" + Messages.NOT_VALID_EMAIL + "}");
        validators.add(v);

        model.setValidators(validators);

        return model;
    }
}
