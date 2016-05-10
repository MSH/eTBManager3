package org.msh.etbm.test.services;


import org.junit.Assert;
import org.msh.etbm.commons.entities.EntityService;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.test.AuthenticatedTest;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;

/**
 * Base class with common entity service tests, like create, find one entity, update entity,
 * delete entity, return a list of many and unique fields
 *
 * Created by rmemoria on 7/2/16.
 */
public abstract class CommonEntityServiceTests extends AuthenticatedTest {

    // the entity class managed by the service
    private Class entityClass;
    // the form data used to return specific values
    private Class formDataClass;
    // the data class to be returned by the service (findOne)
    private Class dataClass;
    private EntityService service;


    /**
     * Single constructor passing the classes to be used during tests
     * @param entityClass the entity class managed by the service
     * @param formDataClass the form data used as argument to create or update the entity
     * @param dataClass the data class returned by the service
     */
    public CommonEntityServiceTests(Class entityClass, Class formDataClass, Class dataClass) {
        this.entityClass = entityClass;
        this.formDataClass = formDataClass;
        this.dataClass = dataClass;
    }

    /**
     * Set the entity service in use. Must be called before start the test
     * @param service the instance of the entity service to test
     */
    protected void setEntityService(EntityService service) {
        this.service = service;
    }


    /**
     * Create and retrieve the entity using the {@link EntityService#create(Object)} and
     * {@link EntityService#findOne(UUID, Class)} methods using the given property values.
     * It is expected that the formDataClass will use the Optional class as the property types,
     * so they are automatically set by reflection when creating the form data
     * @param props the properties and its values to create the form data request
     * @param uniqueProps the list of unique property names to check if unique validation is working
     * @param ignorePropsOnCheck the list of properties to ignore when checking if data is returned
     * @return the ID created for the entity
     */
    protected TestResult testCreateAndFindOne(Map<String, Object> props, List<String> uniqueProps, String ignorePropsOnCheck) {
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
        assertTrue(res.getEntityClass().isAssignableFrom(entityClass));
        assertNull(res.getLogDiffs());
        assertNotNull(res.getLogValues());

        // search for item
        Object data = service.findOne(res.getId(), formDataClass);


        String[] propsToIgnore = ignorePropsOnCheck != null ? ignorePropsOnCheck.split(",") : new String[0];

        assertObjectProperties(data, props, ignorePropsOnCheck);

        if (uniqueProps != null) {
            testUnique(req, uniqueProps);
        }

        // return the data to be tested by the caller
        data = service.findOne(res.getId(), dataClass);

        // compare returned ID
        UUID id = (UUID)ObjectUtils.getProperty(data, "id");

        assertEquals(id, res.getId());

        return new TestResult(id, data);
    }


    /**
     * Test updating by using the given ID and property values. The entity is updated using
     * the {@link EntityService#update(UUID, Object)} and it is checked if updated was really
     * done retrieving and compare the values using the {@link EntityService#findOne(UUID, Class)} method
     * @param id the ID of the entity to be updated
     * @param props the parameter and its new values to be set in the entity
     */
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
        assertTrue(res.getEntityClass().isAssignableFrom(entityClass));
        assertNotNull(res.getEntityName());

        // get the value again
        Object data = service.findOne(id, formDataClass);

        assertObjectProperties(data, props, null);
    }


    /**
     * Assert data of the object to given property values
     * @param data the object data
     * @param props the property values to assert to
     */
    protected void assertObjectProperties(Object data, Map<String, Object> props, String propsToIgnore) {
        String[] ignoreList = propsToIgnore != null ? propsToIgnore.split(",") : new String[0];

        // compare with values set
        for (String prop: props.keySet()) {
            if (Arrays.binarySearch(ignoreList, prop) >= 0) {
                continue;
            }

            Object val = props.get(prop);

            Object val2 = ObjectUtils.getProperty(data, prop);
            if (val2 instanceof Optional) {
                val2 = ((Optional) val2).get();
            }

            // values are collections ?
            if (val instanceof Collection) {
                // get the collections
                Collection lst1 = (Collection)val;
                Collection lst2 = (Collection)val2;
                // check if collections have the same number of elements
                assertEquals(lst1.size(), lst2.size());
                // compare items
                for (Object it: lst1) {
                    assertTrue(lst2.contains(it));
                }
            } else {
                // not a collection, so compare a single value
                assertEquals(val, val2);
            }
        }
    }


    /**
     * Teste the delete operation by using the {@link EntityService#delete(UUID)} and
     * checking if the {@link EntityService#findOne(UUID, Class)} will raise an {@link EntityNotFoundException}
     * exception, indicating that the entity was really deleted
     * @param id the entity ID
     */
    protected void testDelete(UUID id) {
        ServiceResult res = service.delete(id);

        // assert returned values
        assertNotNull(res);
        assertNotNull(res.getId());
        assertNotNull(res.getLogValues());
        assertNull(res.getLogDiffs());
        assertTrue(res.getEntityClass().isAssignableFrom(entityClass));
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


    protected void testUnique(Object request,  List<String> uniqueProps) {
        try {
            ServiceResult res = service.create(request);
            fail("Should generate an EntityValidationException because of unique field constraints");
        }
        catch (EntityValidationException e) {
            assertThat(e, isA(EntityValidationException.class));
        }
    }

    /**
     * Create an instance of the form data to be used in create and update operations
     * @return empty instance of the form data
     */
    protected Object createFormData() {
        return ObjectUtils.newInstance(formDataClass);
    }


    /**
     * Simple wrapper function to create a new entity and return its ID
     * @param service the instance of {@link EntityService} component
     * @param req the object with the request to create the new entity
     * @return the ID of the new entity in the UUID format
     */
    protected UUID create(EntityService service, Object req) {
        ServiceResult res = service.create(req);
        return res.getId();
    }

}
