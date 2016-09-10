package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.indicators.datatable.DataTableUtils;

import static org.junit.Assert.*;

/**
 * Test the {@link DataTableUtils} class
 *
 * Created by rmemoria on 9/9/16.
 */
public class DataTableUtilsTest {

    @Test
    public void compareArraysTest() {
        Object[] a = {10, "Rio"};
        Object[] b = {10, "Rio"};

        // compare equal arrays
        int res = DataTableUtils.compareArray(a, b);
        assertEquals(0, res);

        // a array is before b
        a[1] = "A";

        String s = "a";
        res = DataTableUtils.compareArray(a, b);
        assertEquals(-1, res);

        // a array is after b
        a[1] = "BBB";
        b[1] = "AA";
        res = DataTableUtils.compareArray(a, b);
        assertEquals(1, res);

        // check with arrays of different size
        Object[] c = {50, "Test", "Extra"};
        res = DataTableUtils.compareArray(a, c);
        assertEquals(-1, res);

        // compare arrays with different size but with common elements being equals
        c[0] = a[0];
        c[1] = a[1];
        res = DataTableUtils.compareArray(a, c);
        assertEquals(-1, res);

        // test charcase
        a[1] = a[1].toString().toLowerCase();
        c[1] = c[1].toString().toUpperCase();
        res = DataTableUtils.compareArray(a, c);
        assertEquals(1, res);
    }

    @Test
    public void compareObjectTest() {
        String a = "Rio de Janeiro";
        String b = a;

        boolean res = DataTableUtils.equalValue(a, b);
        assertEquals(true, res);

        b = "São Paulo";
        res = DataTableUtils.equalValue(a, b);
        assertEquals(false, res);
    }
}
