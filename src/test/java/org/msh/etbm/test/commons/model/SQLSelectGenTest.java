package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.models.data.JSFuncValue;
import org.msh.etbm.commons.models.data.JSFunction;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.Validator;
import org.msh.etbm.commons.models.data.fields.*;
import org.msh.etbm.commons.models.db.SQLQueryInfo;
import org.msh.etbm.commons.models.db.SQLQuerySelectionBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * Test of the {@link SQLQuerySelectionBuilder}
 * Created by rmemoria on 9/7/16.
 */
public class SQLSelectGenTest {

    @Test
    public void selectGenTest() {
        Model model = createModel();

        SQLQuerySelectionBuilder gen = new SQLQuerySelectionBuilder();
        gen.setDisplaying(true);
        gen.setCustomTableSuffix("test");
        SQLQueryInfo res = gen.generate(model, model.getTable() + ".id = :id", UUID.randomUUID());
        System.out.println(res.getSql());
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

        FKRegimenField fldRegimen = new FKRegimenField();
        fldRegimen.setName("regimen");
        fldRegimen.setDbFieldName("regimen_id");
        fldRegimen.setCustom(true);
        fields.add(fldRegimen);

        model.setFields(fields);

        List<Validator> validators = new ArrayList<>();
        Validator v = new Validator();
        v.setRule(new JSFunction("this.name == 'Ricardo' ? this.city == 'Rio de Janeiro' : true"));
        v.setMessage("${" + Messages.NOT_VALID_EMAIL + "}");
        validators.add(v);

        model.setValidators(validators);

        return model;
    }
}
