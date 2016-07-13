package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.msh.etbm.Application;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.BoolField;
import org.msh.etbm.commons.models.data.fields.StringField;
import org.msh.etbm.commons.models.db.DataLoader;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.commons.models.db.SQLQueryInfo;
import org.msh.etbm.commons.models.db.SQLQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by rmemoria on 11/7/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebAppConfiguration
public class DataLoaderTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void testLoad() {
        Model model = createModel();
        SQLQueryBuilder gen = new SQLQueryBuilder();
        SQLQueryInfo data = gen.generate(model, null);

        System.out.println(data.getSql());

        DataLoader loader = new DataLoader();
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
