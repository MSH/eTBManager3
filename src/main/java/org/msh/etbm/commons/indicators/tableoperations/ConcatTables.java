package org.msh.etbm.commons.indicators.tableoperations;

import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;

/**
 * Concatenates two tables vertically, i.e, creates a new {@link DataTableImpl}
 * which will contain all rows of both data tables. The two source data tables
 * must have the same columns
 *
 * @author Ricardo Memoria
 *
 */
public class ConcatTables {

    /**
     * Concatenate the rows of two data tables
     * @param tbl1
     * @param tbl2
     * @return a new instance of the {@link DataTableImpl} containing all rows
     */
    public static DataTable concatenate(DataTable tbl1, DataTable tbl2) {
        DataTableImpl target = new DataTableImpl(tbl1.getColumnCount(), 0);
        insertRows(target, tbl1);
        insertRows(target, tbl2);
        return target;
    }


    /**
     * Insert rows from data table source into data table target
     * @param target
     * @param source
     * @return
     */
    public static void insertRows(DataTable target, DataTable source) {
        // check if source table fits on target table
        while (target.getColumnCount() < source.getColumnCount()) {
            target.addColumn();
        }

        for (Row row: source.getRows()) {
            Row trow = target.addRow();
            int index = 0;
            for (Object value: row.getValues()) {
                trow.setValue(index++, value);
            }
        }
    }
}
