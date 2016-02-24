package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.Tag;
import org.msh.etbm.services.admin.tags.TagData;
import org.msh.etbm.services.admin.tags.TagFormData;
import org.msh.etbm.services.admin.tags.TagService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

        UUID id = testCreateAndFindOne(props, lst);

        // test update
        props.clear();
        props.put("name", "My tag2");
        props.put("sqlCondition", "a.state > 3");
        props.put("consistencyCheck", true);
        props.put("active", false);
        props.put("dailyUpdate", true);

        testUpdate(id, props);

        // test delete
        testDelete(id);
    }

    @Test(expected = EntityValidationException.class)
    public void testInvalidSqlCondition(){
        setEntityService(tagService);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", "My tag3");
        props.put("sqlCondition", "a.statee > 2");
        props.put("consistencyCheck", false);
        props.put("active", true);
        props.put("dailyUpdate", false);

        // unique properties
        List<String> lst = new ArrayList<>();
        lst.add("name");

        testCreateAndFindOne(props, lst);
    }
}
