package org.msh.etbm.test.commons.entities;

import org.junit.Test;
import org.msh.etbm.commons.ErrorMessages;
import org.msh.etbm.commons.entities.dao.EntityDAO;
import org.msh.etbm.commons.entities.dao.EntityDAOFactory;
import org.msh.etbm.db.entities.Source;
import org.msh.etbm.services.admin.sources.SourceFormData;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.*;


/**
 * Created by rmemoria on 8/4/16.
 */
public class EntityDAOTest extends AuthenticatedTest {

    @Autowired
    EntityDAOFactory factory;

    @Test
    @Transactional
    public void crudTest() {
        // using source class because it is simple to test
        EntityDAO<Source> dao = factory.newDAO(Source.class);

        assertEquals(dao.getEntityClass(), Source.class);

        SourceFormData data = new SourceFormData();

        data.setName(Optional.of("Any random name " + System.currentTimeMillis()));
        data.setActive(Optional.of(true));
        data.setShortName(Optional.of("Short name " + System.currentTimeMillis()));

        dao.map(data);

        // check if mapping was done
        assertNotNull(dao.getEntity());

        Source source = dao.getEntity();
        assertEquals(source.getName(), data.getName().get());
        assertEquals(source.getShortName(), data.getShortName().get());
        assertEquals(source.isActive(), data.getActive().get());

        // check if validation is done
        assertTrue(dao.validate());

        dao.save();

        // check if save method generated a new ID
        assertNotNull(dao.getId());
        assertNotNull(dao.getEntity().getWorkspace());
        assertEquals(dao.getErrors().getErrorCount(), 0);
        assertTrue(dao.isValid());

        assertEquals(dao.getId(), dao.getEntity().getId());

        // get saved entity
        source = dao.getEntity();

        // check if entity is erased from memory
        dao.setId(null);
        assertTrue(dao.isNew());
        assertTrue(dao.isValid());
        assertNull(dao.getId());
        assertEquals(dao.getErrors().getErrorCount(), 0);

        // create a new dao
        dao = factory.newDAO(Source.class);
        dao.setId(source.getId());

        // check if entity can be loaded in another DAO instance
        Source source2 = dao.getEntity();
        assertEquals(source.getId(), source2.getId());
        assertEquals(source.getName(), source2.getName());
        assertEquals(source.getShortName(), source2.getShortName());
        assertEquals(source.isActive(), source2.isActive());

        // delete entity
        dao.delete();

        // try to load again the entity
        try {
            dao.setId(source2.getId());
            fail("Entity should be removed");
        } catch (EntityNotFoundException e) {
            assertThat(e, isA(EntityNotFoundException.class));
        }
    }

    /**
     * Test validation of EntityDAO objects
     */
    @Test
    public void testValidation() {
        // using source class because it is simple to test
        EntityDAO<Source> dao = factory.newDAO(Source.class);

        Source source = dao.getEntity();
        source.setName("Any name");
        source.setActive(true);

        assertFalse(dao.validate());
        assertFalse(dao.isValid());

        assertEquals(dao.getErrors().getErrorCount(), 1);

        // do a second validation to check if validation messages are not accumulated
        assertFalse(dao.validate());
        assertEquals(dao.getErrors().getErrorCount(), 1);

        dao.addError("name", "Don't like it");
        assertEquals(dao.getErrors().getErrorCount(), 2);

        dao.addNotNullError("shortName");
        assertEquals(dao.getErrors().getErrorCount(), 3);

        FieldError fe = dao.getErrors().getFieldError("shortName");
        assertEquals(fe.getCode(), ErrorMessages.REQUIRED);

        source.setShortName("Name");
        assertTrue(dao.validate());
        assertTrue(dao.isValid());
    }
}
