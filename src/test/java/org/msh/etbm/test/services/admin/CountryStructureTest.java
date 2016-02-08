package org.msh.etbm.test.services.admin;

import org.junit.Test;
import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureFormData;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.FieldError;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Unit test of the country structure service
 * Created by rmemoria on 7/2/16.
 */
public class CountryStructureTest extends CommonEntityServiceTests {

    private static final String NAME = "My region";

    @Autowired
    CountryStructureService service;

    public CountryStructureTest() {
        super(CountryStructure.class, CountryStructureFormData.class, CountryStructureData.class);
    }

    /**
     * Create a new entity
     */
    @Test
    public void crudTest() {
        setEntityService(service);

        // test create and find one
        Map<String, Object> props = new HashMap<>();
        props.put("name", NAME);
        props.put("level", 1);

        UUID id = testCreateAndFindOne(props, null);

        // test update
        props.clear();
        props.put("name", "My region 2");

        testUpdate(id, props);

        // test delete
        testDelete(id);
    }


    /**
     * Country structure requires a specific unique value test because name is just unique in
     * the country structure level
     */
    @Test
    public void uniqueTest() {
        // create entity
        CountryStructureFormData req = new CountryStructureFormData();
        req.setName(Optional.of("My Region"));
        req.setLevel(Optional.of(1));

        service.create(req);

        // create another one in a different level (allowed)
        req.setLevel(Optional.of(2));
        service.create(req);

        // create another one in the same level (not allowed)
        try {
            service.create(req);
            fail("Should raise EntityValidationException");
        }
        catch (EntityValidationException e) {
            assertEquals(e.getClass(), EntityValidationException.class);

            // get errors
            List<FieldError> lst = e.getBindingResult().getFieldErrors();
            assertNotNull(lst);
            // assert error
            assertEquals(lst.size(), 1);
            assertEquals(lst.get(0).getCode(), ErrorMessages.NOT_UNIQUE);
        }
    }
}
