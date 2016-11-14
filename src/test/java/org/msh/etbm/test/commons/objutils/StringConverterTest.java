package org.msh.etbm.test.commons.objutils;

import org.apache.commons.beanutils.converters.DateConverter;
import org.junit.Test;
import org.msh.etbm.commons.objutils.StringConverter;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Test the class {@link org.msh.etbm.commons.objutils.StringConverter}
 * Created by rmemoria on 13/11/16.
 */
public class StringConverterTest {

    @Test
    public void testDate() {
        Date dt = new Date();
        String s = StringConverter.dateToString(dt);
        Date dt2 = StringConverter.stringToDate(s);

        assertNotNull(dt2);
        assertTrue(dt2.equals(dt));
    }

    @Test
    public void testFloat() {
        double val = 10.5;
        String s = StringConverter.doubleToString(val);
        assertNotNull(s);

        double val2 = StringConverter.stringToDouble(s);
        assertTrue(val == val2);
    }

    @Test
    public void testBinary() {
        byte[] vals = { 10, 20, 30, 40, 50 };

        String s = StringConverter.bytesToString(vals);

        assertNotNull(s);

        byte[] res = StringConverter.stringToBytes(s);

        assertNotNull(res);
        assertEquals(vals.length, res.length);

        for (int i = 0; i < vals.length; i++) {
            assertEquals(vals[i], res[i]);
        }
    }
}
