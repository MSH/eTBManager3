package org.msh.etbm.commons.indicators.datatable;

import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.datatable.impl.GroupedDataTableImpl;

/**
 * Standard {@link DataTable} factory.
 * @author Ricardo Memoria
 *
 */
public class DataTableFactory {

    /**
     * Create a new instance of the {@link DataTable} interface
     * @return instance of the {@link DataTable}
     */
    public static DataTable newDataTable() {
        return new DataTableImpl();
    }

    /**
     * Create a new instance of the {@link DataTable} interface, but
     * initialized with a number of columns and rows
     * @param colcount number of columns of the data table
     * @param rowcount number of rows of the data table
     * @return instance of the {@link DataTable}
     */
    public static DataTable newDataTable(int colcount, int rowcount) {
        return new DataTableImpl(colcount, rowcount);
    }

    /**
     * Create a new instance of {@link GroupedDataTable}
     * @return instance of {@link GroupedDataTable}
     */
    public static GroupedDataTable newGroupedDataTable() {
        return new GroupedDataTableImpl();
    }
}
