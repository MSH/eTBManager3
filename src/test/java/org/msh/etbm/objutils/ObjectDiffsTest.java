package org.msh.etbm.objutils;

import org.junit.Test;
import org.msh.etbm.commons.entities.DiffValue;
import org.msh.etbm.commons.entities.Diffs;
import org.msh.etbm.commons.entities.ObjectValues;
import org.msh.etbm.commons.objutils.ObjectDiffs;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Test ObjectDiffs class
 *
 * Created by rmemoria on 28/1/16.
 */
public class ObjectDiffsTest {

    /**
     * Check if all properties are read
     */
    @Test
    public void getPropertyValues() {
        Model model = new Model();

        ObjectValues vals = ObjectDiffs.generateValues(model);
        System.out.println(vals.getValues());
        assertEquals(vals.getValues().size(), 5);
    }

    /**
     * Check if objects are the same
     */
    @Test
    public void testEquals() {
        Model model = new Model();
        model.setName("xxx");
        model.setId(1000L);

        // two objects are the same ?
        assert(ObjectDiffs.compareEquals(model, model));

        Model m2 = new Model();

        // objects must be different
        assert(!ObjectDiffs.compareEquals(model, m2));

        // objects must be equal
        // because ID is the key to identify equals objects inside Model class
        m2.setId(model.getId());
        assert(ObjectDiffs.compareEquals(model, m2));
        assert(ObjectDiffs.compareEquals(m2, model));
    }

    /**
     * Test object differences
     */
    @Test
    public void testDiffs() {
        Date dt = new Date();

        // check the same object
        Model model = new Model("name1", 1L, 33, true, dt);
        Diffs diffs = ObjectDiffs.compareOldAndNew(model, model);
        assertEquals(diffs.getValues().size(), 0);

        // check another object but with the same properties
        Model m2 = new Model("name1", 1L, 33, true, dt);
        diffs = ObjectDiffs.compareOldAndNew(model, m2);
        assertEquals(diffs.getValues().size(), 0);

        // set any date
        m2.setLastUpdate(new Date(20000L));
        diffs = ObjectDiffs.compareOldAndNew(model, m2);

        // must have at least one value
        assertEquals(diffs.getValues().size(), 1);
        DiffValue diffVal = diffs.getValues().get("lastUpdate");
        assertNotNull(diffVal);
        assertTrue(diffVal.isDifferent());
        assertEquals(diffVal.getPrevValue(), model.getLastUpdate());
        assertEquals(diffVal.getNewValue(), m2.getLastUpdate());

        // one more change
        m2.setName("name 2");
        m2.setAge(66);
        diffs = ObjectDiffs.compareOldAndNew(model, m2);
        assertEquals(diffs.getValues().size(), 3);
        assertNotNull(diffs.getValues().get("lastUpdate"));
        assertNotNull(diffs.getValues().get("name"));
        assertNotNull(diffs.getValues().get("age"));
    }
}
