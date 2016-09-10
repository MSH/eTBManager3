package org.msh.etbm.test.commons.indicators;

import org.junit.Test;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.query.ColumnQuery;
import org.msh.etbm.commons.indicators.query.DataTableQuery;
import org.msh.etbm.commons.indicators.query.DataTableQueryImpl;
import org.msh.etbm.commons.indicators.query.RowQuery;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by rmemoria on 10/9/16.
 */
public class DataTableQueryTest {

    @Test
    public void testDataTable() {
        DataTableQuery tbl = new DataTableQueryImpl();

        tbl.resize(4, 0);
        assertEquals(4, tbl.getColumnCount());
        assertEquals(0, tbl.getRowCount());

        List<ColumnQuery> lst = tbl.getQueryColumns();
        assertEquals(4, tbl.getColumnCount());

        // set column names
        lst.get(0).setFieldName("ID");
        lst.get(1).setFieldName("Name");
        lst.get(2).setFieldName("BirthDate");
        lst.get(3).setFieldName("Active");

        // add one single row
        Row r = tbl.addRow();
        r.setValue(0, 12345);
        r.setValue(1, "Person name");
        Date dt = DateUtils.newDate(1990, 1, 3);
        r.setValue(2, dt);
        r.setValue(3, true);

        List<RowQuery> rows = tbl.getQueryRows();
        assertNotNull(rows);

        RowQuery rq = rows.get(0);
        assertNotNull(rq);
        
        assertEquals(12345, rq.getValue("ID"));
        assertEquals("Person name", rq.getValue("Name"));
        assertEquals(dt, rq.getValue("BirthDate"));
        assertEquals(true, rq.getValue("Active"));
    }
}
