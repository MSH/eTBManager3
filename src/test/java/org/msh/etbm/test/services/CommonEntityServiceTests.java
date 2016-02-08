package org.msh.etbm.test.services;

import org.hibernate.metamodel.source.annotations.entity.EntityClass;
import org.junit.Assert;
import org.junit.Test;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.test.AbstractAuthenticatedTest;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;

/**
 * Base class with common entity service tests, like create, find one entity, update entity,
 * delete entity, return a list of many and unique fields
 *
 * Created by rmemoria on 7/2/16.
 */
public abstract class CommonEntityServiceTests extends AbstractAuthenticatedTest {

    // the entity class managed by the service
    private Class entityClass;
    // the form data used to return specific values
    private Class formDataClass;
    // the data class to be returned by the service (findOne)
    private Class dataClass;
    private EntityService service;


    public CommonEntityServiceTests(Class entityClass, Class formDataClass, Class dataClass) {
        this.entityClass = entityClass;
        this.formDataClass = formDataClass;
        this.dataClass = dataClass;
    }

    protected void setEntityService(EntityService service) {
        this.service = service;
    }


    protected UUID testCreateAndFindOne(Map<String, Object> props) {
        Object req = createFormData();

        // set the property values
        for (String prop: props.keySet()) {
            ObjectUtils.setProperty(req, prop, Optional.of(props.get(prop)));
        }

        // create a new entity
        ServiceResult res = service.create(req);

        // assert result
        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals(res.getEntityClass(), entityClass);
        assertNull(res.getLogDiffs());
        assertNotNull(res.getLogValues());

        // search for item
        Object data = service.findOne(res.getId(), dataClass);

        // compare returned ID
        UUID id = (UUID)ObjectUtils.getProperty(data, "id");

        assertEquals(id, res.getId());

        // compare the given properties
        for (String prop: props.keySet()) {
            Object val = ObjectUtils.getProperty(data, prop);
            assertEquals(val, props.get(prop));
        }

        return id;
    }


    protected void testUpdate(UUID id, Map<String, Object> props) {
        Object req = createFormData();

        // set the values
        for (String prop: props.keySet()) {
            ObjectUtils.setProperty(req, prop, Optional.of(props.get(prop)));
        }

        // update the value
        ServiceResult res = service.update(id, req);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertNull(res.getLogValues());
        assertNotNull(res.getLogDiffs());
        assertEquals(res.getEntityClass(), entityClass);
        assertNotNull(res.getEntityName());

        // get the value again
        Object data = service.findOne(id, dataClass);

        // compare with values set
        for (String prop: props.keySet()) {
            Object val = props.get(prop);
            Object newval = ObjectUtils.getProperty(data, prop);
            assertEquals(val, newval);
        }
    }


    protected void testDelete(UUID id) {
        ServiceResult res = service.delete(id);

        // assert returned values
        assertNotNull(res);
        assertNotNull(res.getId());
        assertNotNull(res.getLogValues());
        assertNull(res.getLogDiffs());
        assertEquals(res.getEntityClass(), entityClass);
        assertNotNull(res.getEntityName());

        // check if entity was really deleted, trying to load it again
        try {
            service.findOne(id, dataClass);
            Assert.fail("Expected EntityNotFound");
        }
        catch (EntityNotFoundException e) {
            assertThat(e, isA(EntityNotFoundException.class));
        }
    }


    protected void testUnique(UUID id, List<String> props) {
        Object res = service.findOne(id, dataClass);
    }

    protected Object createFormData() {
        return ObjectUtils.newInstance(formDataClass);
    }


}
