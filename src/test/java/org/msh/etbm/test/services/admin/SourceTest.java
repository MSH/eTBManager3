package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.db.entities.Source;
import org.msh.etbm.services.admin.sources.SourceData;
import org.msh.etbm.services.admin.sources.SourceFormData;
import org.msh.etbm.services.admin.sources.SourceService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rmemoria on 8/2/16.
 */
public class SourceTest extends CommonEntityServiceTests {

    public SourceTest() {
        super(Source.class, SourceFormData.class, SourceData.class);
    }

    @Autowired
    SourceService sourceService;

    @Test
    public void executeTest() {
        setEntityService(sourceService);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", "My source");
        props.put("shortName", "MS");

        UUID id = testCreateAndFindOne(props);

        // test update
        props.clear();
        props.put("name", "My source 2");
        props.put("shortName", "MS 2");

        testUpdate(id, props);

        // test delete
        testDelete(id);
    }
}
