package org.msh.etbm.test.commons.objutils;

import org.junit.Test;
import org.msh.etbm.commons.objutils.DiffValue;
import org.msh.etbm.commons.objutils.Diffs;
import org.msh.etbm.commons.objutils.DiffsUtils;
import org.msh.etbm.commons.objutils.ObjectValues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test ObjectDiffs class
 * <p>
 * Created by rmemoria on 28/1/16.
 */
public class DiffsUtilsTest {

    /**
     * Check if all properties are read
     */
    @Test
    public void getPropertyValues() {
        Model model = new Model();

        ObjectValues vals = new ObjectValues(model);
        assertEquals(vals.getValues().size(), 6);
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
        assert (DiffsUtils.compareEquals(model, model));

        Model m2 = new Model();

        // objects must be different
        assert (!DiffsUtils.compareEquals(model, m2));

        // objects must be equal
        // because ID is the key to identify equals objects inside Model class
        m2.setId(model.getId());
        assert (DiffsUtils.compareEquals(model, m2));
        assert (DiffsUtils.compareEquals(m2, model));
    }

    /**
     * Test object differences
     */
    @Test
    public void testDiffs() {
        Date dt = new Date();

        // check the same object
        String[] fruits = {"Banana", "Apple", "Strawberry", "Orange"};
        Model model = new Model("name1", 1L, 33, true, dt, Arrays.asList(fruits));

        Diffs diffs = DiffsUtils.generateDiffs(model, model);
        assertEquals(diffs.getValues().size(), 0);

        // check another object but with the same properties
        String[] fruits2 = {"Banana", "Apple", "Strawberry", "Orange"};
        Model m2 = new Model("name1", 1L, 33, true, dt, Arrays.asList(fruits2));
        diffs = DiffsUtils.generateDiffs(model, m2);
        assertEquals(diffs.getValues().size(), 0);

        // set any date
        m2.setLastUpdate(new Date(20000L));
        diffs = DiffsUtils.generateDiffs(model, m2);

        // must have at least one value
        assertEquals(diffs.getValues().size(), 1);
        DiffValue diffVal = diffs.getValues().get("lastUpdate");
        assertNotNull(diffVal);
        assertEquals(diffVal.getPrevValue(), model.getLastUpdate());
        assertEquals(diffVal.getNewValue(), m2.getLastUpdate());

        // one more change
        m2.setName("name 2");
        m2.setAge(66);
        diffs = DiffsUtils.generateDiffs(model, m2);
        assertEquals(diffs.getValues().size(), 3);
        assertNotNull(diffs.getValues().get("lastUpdate"));
        assertNotNull(diffs.getValues().get("name"));
        assertNotNull(diffs.getValues().get("age"));

        String[] fruits3 = {"Apple", "Strawberry", "Orange"};
        m2.setFruits(Arrays.asList(fruits3));
        diffs = DiffsUtils.generateDiffs(model, m2);
        assertEquals(diffs.getValues().size(), 4);
        DiffValue diff = diffs.getValues().get("fruits");
        assertNotNull(diff);
    }

    @Test
    public void testArray() {
        List<Model> lst1 = new ArrayList<>();
        List<Model> lst2 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            lst1.add(new Model("Name " + i, (long) i, 30 + i, false, new Date(), null));
            lst2.add(new Model("Name " + i, (long) i, 30 + i, false, new Date(), null));
        }

        ModelArray ma1 = new ModelArray();
        ma1.setItems(lst1);

        ModelArray ma2 = new ModelArray();
        ma2.setItems(lst2);
        Diffs diffs = DiffsUtils.generateDiffs(ma1, ma2);
        assertNotNull(diffs);
        assertEquals(diffs.getValues().size(), 0);

        lst1.get(0).setId(1000L);
        diffs = DiffsUtils.generateDiffs(ma1, ma2);
        assertEquals(diffs.getValues().size(), 1);

        lst1.get(1).setAge(15);
        diffs = DiffsUtils.generateDiffs(ma1, ma2);
        assertEquals(diffs.getValues().size(), 1);
    }

}
