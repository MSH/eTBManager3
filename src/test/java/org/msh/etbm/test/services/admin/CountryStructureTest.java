package org.msh.etbm.test.services.admin;

import org.junit.Assert;
import org.junit.Test;
import org.msh.etbm.test.AbstractAuthenticatedTest;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.services.admin.admunits.CountryStructureData;
import org.msh.etbm.services.admin.admunits.CountryStructureFormData;
import org.msh.etbm.services.admin.admunits.CountryStructureService;
import org.msh.etbm.test.services.CommonEntityServiceTests;
import org.msh.etbm.web.api.StandardResult;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


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

        UUID id = testCreateAndFindOne(props);

        // test update
        props.clear();
        props.put("name", "My region 2");

        testUpdate(id, props);

        // test delete
        testDelete(id);
    }

}
