package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.admin.tags.TagFormData;
import org.msh.etbm.services.admin.tags.TagService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.msh.etbm.test.services.TestResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by msantos on 2/24/16.
 */
public class TagTest extends CommonEntityServiceTests {

    public TagTest() {
        super(Tag.class, TagFormData.class, TagData.class);
    }

    @Autowired
    TagService tagService;

    @Test
    public void executeTest() {
        setEntityService(tagService);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", "My tag");
        props.put("sqlCondition", "a.state > 2");
        props.put("consistencyCheck", false);
        props.put("active", true);
        props.put("dailyUpdate", false);

        // unique properties
        List<String> lst = new ArrayList<>();
        lst.add("name");

        TestResult res = testCreateAndFindOne(props, lst, null);

        // test update
        props.clear();
        props.put("name", "My tag2");
        props.put("sqlCondition", "a.state > 3");
        props.put("consistencyCheck", true);
        props.put("active", false);
        props.put("dailyUpdate", true);

        testUpdate(res.getId(), props);

        // test delete
        testDelete(res.getId());
    }

    @Test(expected = EntityValidationException.class)
    public void testInvalidSqlCondition(){
        setEntityService(tagService);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", "My tag3");
        // THERE IS AN ERROR, BUT IT'S PART OF THE TEST
        props.put("sqlCondition", "a.invalidFIELD > 2");
        props.put("consistencyCheck", false);
        props.put("active", true);
        props.put("dailyUpdate", false);

        // unique properties
        List<String> lst = new ArrayList<>();
        lst.add("name");

        testCreateAndFindOne(props, lst, null);
    }
}
