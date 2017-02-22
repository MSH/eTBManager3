package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.indicators.datatable.DataTableUtils;
import org.msh.etbm.commons.indicators.datatable.GroupedDataTable;
import org.msh.etbm.commons.indicators.datatable.impl.GroupedDataTableImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

        tbl.setValue(key1, key1, 11);
        assertEquals(11, tbl.getValue(key1, key1));

        assertEquals(1, tbl.getRowCount());
        assertEquals(1, tbl.getColumnCount());

        assertNull(tbl.getValue(key1, key2));
        assertNull(tbl.getValue(key2, key1));

        tbl.setValue(key3, key1, 31);
        assertEquals(31, tbl.getValue(key3, key1));

        // insert a key in the middle of key1 and key3
        tbl.setValue(key2, key1, 21);
        assertEquals(21, tbl.getValue(key2, key1));

        // check key of the row
        List<Object[]> keys = tbl.getRowKeys();
        assertEquals(1, keys.size());

        Object[] vals = keys.get(0);
        assertEquals(0, DataTableUtils.compareArray(key1, vals));

        // check if keys of the columns are in the order
        // dez-2016 - DataTable cannot assume the order of the keys, so they are included
        // at the end of the row/col
//        keys = tbl.getColumnKeys();
//        assertEquals(3, keys.size());
//
//        assertEquals(0, DataTableUtils.compareArray(key1, keys.get(0)));
//        assertEquals(0, DataTableUtils.compareArray(key2, keys.get(1)));
//        assertEquals(0, DataTableUtils.compareArray(key3, keys.get(2)));

        // check values
        List values = tbl.getRowValues(0);
//        assertEquals(11, values.get(0));
//        assertEquals(21, values.get(1));
//        assertEquals(31, values.get(2));

        // include one more row
        tbl.setValue(key1, key3, 13);
        assertEquals(13, tbl.getValue(key1, key3));
        assertEquals(2, tbl.getRowCount());

        values = tbl.getRowValues(1);
        assertEquals(3, values.size());
        assertEquals(13, values.get(0));

        // insert one row between key1 and key3
        tbl.setValue(key2, key2, 22);
        assertEquals(22, tbl.getValue(key2, key2));

        // delete a column
        tbl.removeColumn(key3);
        assertEquals(2, tbl.getColumnCount());
        keys = tbl.getColumnKeys();
        assertEquals(0, DataTableUtils.compareArray(key1, keys.get(0)));
        assertEquals(0, DataTableUtils.compareArray(key2, keys.get(1)));
        assertEquals(2, tbl.getRowValues(0).size());

        // insert a column at the end
        tbl.setValue(key4, key1, 42);
        keys = tbl.getColumnKeys();
        assertEquals(0, DataTableUtils.compareArray(key1, keys.get(0)));
        assertEquals(0, DataTableUtils.compareArray(key2, keys.get(1)));
        assertEquals(0, DataTableUtils.compareArray(key4, keys.get(2)));
        assertEquals(3, tbl.getRowValues(0).size());

        // insert the key3 again
        tbl.setValue(key3, key3, 33);
        assertEquals(33, tbl.getValue(key3, key3));
        assertEquals(tbl.getColumnCount(), tbl.getColumnKeys().size());
        assertEquals(tbl.getRowCount(), tbl.getRowKeys().size());
        assertEquals(4, tbl.getColumnKeys().size());
        assertEquals(3, tbl.getRowCount());

//        values = tbl.getRowValues(2);
//        assertEquals(33, values.get(2));

        Object[] keya0b0 = {"a0", "b0"};
        tbl.setValue(keya0b0, key1, "TOT");
        keys = tbl.getColumnKeys();
        System.out.println(keys);
    }
}
