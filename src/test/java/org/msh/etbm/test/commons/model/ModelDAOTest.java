package org.msh.etbm.test.commons.model;

import org.junit.Test;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.models.ModelDAO;
import org.msh.etbm.commons.models.ModelDAOFactory;
import org.msh.etbm.commons.models.ModelDAOResult;
import org.msh.etbm.commons.models.db.RecordData;
import org.msh.etbm.test.AuthenticatedTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Test the new {@link org.msh.etbm.commons.models.ModelDAO} class, to support CRUD operations
 * in a data table model
 *
 * Created by rmemoria on 14/7/16.
 */
public class ModelDAOTest extends AuthenticatedTest {

    private static final String NAME = "Ricardo ";
    private static final String MIDDLENAME = "Memoria";
    private static final String LASTNAME = "Lima";
    private static final Date BIRTHDATE = DateUtils.newDate(1971, 11, 13);
    private static final String GENDER = "MALE";

    private static final String UPDT_NAME = "Karla";
    private static final String UPDT_MIDDLENAME = "Neves";


    @Autowired
    ModelDAOFactory factory;

    @Test
    public void testCRUD() {
        ModelDAO dao = factory.create("patient");

        // create model (including 10 records)
        List<ModelDAOResult> ids = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> p = new HashMap<>();
            // use i to avoid unique constraint
            p.put("name", NAME + (i + 1));
            p.put("middleName", MIDDLENAME);
            p.put("lastName", LASTNAME);
            p.put("birthDate", BIRTHDATE);
            p.put("gender", GENDER);
            ModelDAOResult res = dao.insert(p);

            assertNotNull(res);
            assertNotNull(res.getId());
            assertNull(res.getErrors());

            ids.add(res);
        }

        ModelDAOResult res = ids.get(0);

        UUID id = res.getId();

        // find record
        RecordData p2 = dao.findOne(id);
        assertNotNull(p2);
        assertNotNull(p2.getId());
        assertNotNull(p2.getValues());
        assertEquals(p2.getString("name"), NAME + 1);
        assertEquals(p2.getString("middleName"), MIDDLENAME);
        assertEquals(p2.getString("lastName"), LASTNAME);
        assertEquals(p2.getDate("birthDate"), BIRTHDATE);
        assertEquals(p2.getString("gender"), GENDER);

        // update record
        Map<String, Object> vals = p2.getValues();
        vals.put("name", UPDT_NAME);
        vals.put("middleName", UPDT_MIDDLENAME);
        res = dao.update(p2.getId(), vals);
        assertNotNull(res);

        // find to check the changes
        p2 = dao.findOne(p2.getId());
        assertNotNull(p2);
        assertEquals(p2.getString("name"), UPDT_NAME);
        assertEquals(p2.getString("middleName"), UPDT_MIDDLENAME);

        // find many
        List<RecordData> list = dao.findMany(false, null, null);
        assertEquals(10, list.size());

        // code cleanup
        // delete record
        for (ModelDAOResult r: ids) {
            dao.delete(r.getId());
        }

        // try to find it again
        RecordData rd = dao.findOne(res.getId());
        assertNull(rd);
    }
}
