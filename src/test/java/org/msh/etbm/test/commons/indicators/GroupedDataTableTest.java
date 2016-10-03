package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.indicators.datatable.DataTableUtils;
import org.msh.etbm.commons.indicators.datatable.GroupedDataTable;
import org.msh.etbm.commons.indicators.datatable.impl.GroupedDataTableImpl;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the {@link GroupedDataTable}
 * Created by rmemoria on 2/10/16.
 */
public class GroupedDataTableTest {

    @Test
    public void test() {
        GroupedDataTable tbl = new GroupedDataTableImpl();

        Object[] key1 = {"a0", "b0", "c0"};
        Object[] key2 = {"a0", "b0", "c1"};
        Object[] key3 = {"a0", "b1", "c0"};
        Object[] key4 = {"a0", "b1", "c1"};

        tbl.setValue(key1, key1, 1);
        assertEquals(1, tbl.getValue(key1, key1));

        assertEquals(1, tbl.getRowCount());
        assertEquals(1, tbl.getColumnCount());

        assertNull(tbl.getValue(key1, key2));
        assertNull(tbl.getValue(key2, key1));

        tbl.setValue(key3, key1, 3);
        assertEquals(3, tbl.getValue(key3, key1));

        // insert a key in the middle of key1 and key3
        tbl.setValue(key2, key1, 2);
        assertEquals(2, tbl.getValue(key2, key1));

        // check key of the row
        List<Object[]> keys = tbl.getRowKeys();
        assertEquals(1, keys.size());

        Object[] vals = keys.get(0);
        assertEquals(0, DataTableUtils.compareArray(key1, vals));

        // check if keys of the columns are in the order
        keys = tbl.getColumnKeys();
        assertEquals(3, keys.size());

        assertEquals(0, DataTableUtils.compareArray(key1, keys.get(0)));
        assertEquals(0, DataTableUtils.compareArray(key2, keys.get(1)));
        assertEquals(0, DataTableUtils.compareArray(key3, keys.get(2)));

        // check values
        List values = tbl.getRowValues(0);
        assertEquals(1, values.get(0));
        assertEquals(2, values.get(1));
        assertEquals(3, values.get(2));

        // include one more row
        tbl.setValue(key1, key2, 11);
        assertEquals(11, tbl.getValue(key1, key2));
        assertEquals(2, tbl.getRowCount());

        values = tbl.getRowValues(1);
        assertEquals(3, values.size());
        assertEquals(11, values.get(0));
    }
}
