package org.msh.etbm.objutils;

import org.junit.Test;
import org.msh.etbm.commons.objutils.ObjectAccessException;
import org.msh.etbm.commons.objutils.ObjectUtils;

import static org.junit.Assert.*;

/**
 * Test ObjectUtils class
 * Created by rmemoria on 28/1/16.
 */
public class ObjectUtilsTest {

    private static final String NAME = "Richard Memory";
    private static final int AGE = 33;

    /**
     * Test reading and writing to property
     */
    @Test
    public void readAndWrite() {
        Model model = new Model();

        ObjectUtils.setProperty(model, "name", NAME);
        ObjectUtils.setProperty(model, "age", AGE);

        assertEquals(model.getName(), NAME);
        assertEquals(model.getAge(), AGE);

        assertEquals(ObjectUtils.getProperty(model, "name"), NAME);
        assertEquals(ObjectUtils.getProperty(model, "age"), AGE);
    }

    /**
     * Test setting the wrong type to the property
     */
    @Test(expected = ObjectAccessException.class)
    public void invalidWriteType() {
        Model model = new Model();

        ObjectUtils.setProperty(model, "age", NAME);
    }

    /**
     * Testing creating a new instance of a class
     */
    @Test
    public void newInstance() {
        Model model = ObjectUtils.newInstance(Model.class);

        assert(model != null);
    }
}
