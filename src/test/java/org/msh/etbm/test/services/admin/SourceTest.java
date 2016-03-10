package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.db.entities.Source;
import org.msh.etbm.services.admin.sources.SourceData;
import org.msh.etbm.services.admin.sources.SourceFormData;
import org.msh.etbm.services.admin.sources.SourceService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.msh.etbm.test.services.TestResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Test of CRUD operations in {@link SourceService}
 *
 * Created by rmemoria on 8/2/16.
 */
public class SourceTest extends CommonEntityServiceTests {

    @Autowired
    SourceService sourceService;

    public SourceTest() {
        super(Source.class, SourceFormData.class, SourceData.class);
    }

    @Test
    public void executeTest() {
        setEntityService(sourceService);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", "My source");
        props.put("shortName", "MS");

        // unique properties
        List<String> lst = new ArrayList<>();
        lst.add("name");
        lst.add("shortName");

        TestResult res = testCreateAndFindOne(props, lst, null);

        // test update
        props.clear();
        props.put("name", "My source 2");
        props.put("shortName", "MS 2");

        testUpdate(res.getId(), props);

        // test delete
        testDelete(res.getId());
    }
}
