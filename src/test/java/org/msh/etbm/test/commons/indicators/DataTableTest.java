package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.indicators.datatable.Column;
import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.DataTableFactory;
import org.msh.etbm.commons.indicators.datatable.Row;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test the implementation of the {@link DataTable}
 * Created by rmemoria on 9/9/16.
 */
public class DataTableTest {

    @Test
    public void testDataTable() {
        DataTable tbl = DataTableFactory.newDataTable();
        assertNotNull(tbl);
        assertEquals(0, tbl.getColumnCount());
        assertEquals(0, tbl.getRowCount());

        checkNoColumn(tbl);
        checkNoRow(tbl);

        // add a column
        Column c = tbl.addColumn();
        assertNotNull(c);
        checkNoRow(tbl);
        assertEquals(1, tbl.getColumnCount());
        assertEquals(0, tbl.getRowCount());

        // add a row
        Row r = tbl.addRow();
        assertNotNull(r);
        assertEquals(1, tbl.getColumnCount());
        assertEquals(1, tbl.getRowCount());

        // get null values of non initialized table
        Object val = tbl.getValue(0, 0);
        assertNull(val);

        // set table value
        Date dt = new Date();
        tbl.setValue(0, 0, dt);
        val = tbl.getValue(0, 0);
        assertNotNull(val);
        assertEquals(val, dt);

        // add a new column
        Column c2 = tbl.addColumn();
        assertNotNull(c2);
        assertEquals(2, tbl.getColumnCount());
        assertEquals(val, tbl.getValue(0, 0));

        String s = "COL2";
        tbl.setValue(1, 0, s);
        assertEquals(val, tbl.getValue(0, 0));
        assertEquals(s, tbl.getValue(1, 0));

        // add a new row
        Row r2 = tbl.addRow();
        assertNotNull(r2);

        // set value to the row
        String sTot = "TOTAL";
        tbl.setValue(1, 1, sTot);
        assertEquals(sTot, tbl.getValue(1, 1));
        // check if values were not changed
        assertEquals(val, tbl.getValue(0, 0));
        assertEquals(s, tbl.getValue(1, 0));

        assertEquals(2, tbl.getColumnCount());
        assertEquals(2, tbl.getRowCount());

        assertEquals(2, tbl.getRow(0).getValues().size());
        assertEquals(2, tbl.getColumn(0).getValues().size());

        // remove column 0
        Object v0 = tbl.getValue(1, 0);
        Object v1 = tbl.getValue(1, 1);
        tbl.removeColumn(0);

        // check if column was properly shifted
        assertEquals(v0, tbl.getValue(0, 0));
        assertEquals(v1, tbl.getValue(0, 1));
    }


    @Test
    public void testRemove() {
        DataTable tbl = createTable();
        tbl.removeColumn(1);
        tbl.removeRow(0);

        // check if values were correctly shifted
        for (int r = 0; r < 5; r++) {
            // first col just contain value 0
            assertEquals(0, tbl.getValue(0, r));
            // 3rd and 4th cols became 2nd and 3rd cols
            // row 0 was removed, so values were shifted in 1
            assertEquals(2 * (r + 1), tbl.getValue(1, r));
            assertEquals(3 * (r + 1), tbl.getValue(2, r));
        }
    }

    @Test
    public void testAddColumnRow() {
        DataTable tbl = createTable();
        tbl.insertColumn(1);

        for (int r = 0; r < 6; r++) {
            // col 0 maintain its value
            assertEquals(0, tbl.getValue(0, r));
            // new col
            assertNull(tbl.getValue(1, r));
            // cols > 1, value was shifted to the right
            assertEquals(1 * r, tbl.getValue(2, r));
            assertEquals(2 * r, tbl.getValue(3, r));
            assertEquals(3 * r, tbl.getValue(4, r));
        }

        // restore values
        tbl.removeColumn(1);
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(r * c, tbl.getValue(c, r));
            }
        }

        // insert a row
        tbl.insertRow(1);
        for (int c = 0; c < 4; c++) {
            assertEquals(0, tbl.getValue(c, 0));
            // new row
            assertNull(tbl.getValue(c, 1));
            // shifted rows
            assertEquals(c * 1, tbl.getValue(c, 2));
            assertEquals(c * 2, tbl.getValue(c, 3));
            assertEquals(c * 3, tbl.getValue(c, 4));
            assertEquals(c * 4, tbl.getValue(c, 5));
        }

        // restore original values
        tbl.removeRow(1);
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                assertEquals(r * c, tbl.getValue(c, r));
            }
        }

        // add a new column in position 0
        int count = tbl.getColumnCount();
        tbl.insertColumn(0);
        assertEquals(count + 1, tbl.getColumnCount());
        for (int r = 0; r < tbl.getRowCount(); r++) {
            for (int c = 1; c < tbl.getColumnCount(); c++) {
                assertEquals(r * (c - 1), tbl.getValue(c, r));
            }
        }

        // add a new row at position 0
        count = tbl.getRowCount();
        tbl.insertRow(0);
        assertEquals(count + 1, tbl.getRowCount());
        for (int r = 1; r < tbl.getRowCount(); r++) {
            for (int c = 1; c < tbl.getColumnCount(); c++) {
                assertEquals((r - 1) * (c - 1), tbl.getValue(c, r));
            }
        }
    }

    @Test
    public void testRow() {
        DataTable tbl = createTable();
        Row row = tbl.getRow(1);
        assertEquals(1, row.getIndex());

        List lst = row.getValues();
        for (int c = 0; c < 4; c++) {
            assertEquals(c, row.getValue(c));
            assertEquals(c, lst.get(c));
        }

        // test columns index
        int[] cols = {0, 2, 3};
        Object[] vals = row.getValues(cols);
        assertEquals(0, vals[0]);
        assertEquals(2, vals[1]);
        assertEquals(3, vals[2]);

        // set new values and check its values
        Object[] newvals = {1000, 2000, 3000};
        row.setValues(cols, newvals);
        assertEquals(newvals[0], tbl.getValue(cols[0], row.getIndex()));
        assertEquals(newvals[1], tbl.getValue(cols[1], row.getIndex()));
        assertEquals(newvals[2], tbl.getValue(cols[2], row.getIndex()));
    }

    @Test
    public void testColumns() {
        DataTable tbl = createTable();
        Column col = tbl.getColumn(1);
        assertEquals(1, col.getIndex());

        List lst = col.getValues();
        for (int r = 0; r < 6; r++) {
            assertEquals(r, col.getValue(r));
            assertEquals(r, lst.get(r));
        }

        // test columns index
        int[] rows = {0, 2, 5};
        Object[] vals = col.getValues(rows);
        assertEquals(0, vals[0]);
        assertEquals(2, vals[1]);
        assertEquals(5, vals[2]);

        // set new values and check its values
        Object[] newvals = {1000, 2000, 3000};
        col.setValues(rows, newvals);
        assertEquals(newvals[0], tbl.getValue(col.getIndex(), rows[0]));
        assertEquals(newvals[1], tbl.getValue(col.getIndex(), rows[1]));
        assertEquals(newvals[2], tbl.getValue(col.getIndex(), rows[2]));
    }

    @Test
    public void testSort() {
        DataTable tbl = createTable();

        // invert the order of the rows
        tbl.sortRows((row1, row2) -> -Integer.compare((int)row1.getValue(1), (int)row2.getValue(1)));

        for (int r = 0; r < 6; r++) {
            assertEquals(5 - r, tbl.getValue(1, r));
        }
    }

    /**
     * Create a data table for easy testing
     * @return instance of {@link DataTable}
     */
    protected DataTable createTable() {
        DataTable tbl = DataTableFactory.newDataTable(4, 6);

        assertNotNull(tbl);
        assertEquals(4, tbl.getColumnCount());
        assertEquals(6, tbl.getRowCount());

        // initialize values
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 6; r++) {
                tbl.setValue(c, r, c * r);
            }
        }

        // check if values were set
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 6; r++) {
                assertEquals(c * r, tbl.getValue(c, r));
            }
        }

        return tbl;
    }

    protected void checkNoColumn(DataTable tbl) {
        try {
            tbl.getColumn(0);
            throw new RuntimeException("Expect to generate an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
        }
    }

    protected void checkNoRow(DataTable tbl) {
        try {
            tbl.getRow(0);
            throw new RuntimeException("Expect to generate an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            //
        }
    }
}
