package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.BoolField;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.commons.models.db.SQLQuerySelectionBuilder;
import org.msh.etbm.commons.models.db.SQLQueryInfo;
import org.msh.etbm.commons.models.db.SQLQueryLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by rmemoria on 11/7/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class SQLQueryLoaderTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void testLoad() {
        Model model = createModel();
        SQLQuerySelectionBuilder gen = new SQLQuerySelectionBuilder();
        SQLQueryInfo data = gen.generate(model, null, UUID.randomUUID());

        System.out.println(data.getSql());

        SQLQueryLoader loader = new SQLQueryLoader();
        List<RecordData> list = loader.loadData(dataSource, data);

        for (RecordData rec: list) {
            System.out.println(rec);
        }
    }


    public Model createModel() {
        Model m = new Model();
        m.setName("source");
        m.setTable("source");

        StringField fldName = new StringField("name");
        StringField fldShortName = new StringField("shortName");
        StringField fldCustomId = new StringField("customId");
        BoolField fldActive = new BoolField("active");

        m.setFields(Arrays.asList(fldName, fldShortName, fldCustomId, fldActive));

        return m;
    }
}
