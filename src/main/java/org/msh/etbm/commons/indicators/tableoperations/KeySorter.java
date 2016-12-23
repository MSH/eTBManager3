package org.msh.etbm.commons.indicators.tableoperations;


import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.Row;
import org.msh.etbm.commons.indicators.keys.Key;

import java.util.Comparator;

/**
 * Sort the content of the data table based on its key values
 * Created by rmemoria on 13/12/16.
 */
public class KeySorter {

    public static void sortByKey(final DataTable tbl) {
        tbl.sortRows(new Comparator<Row>() {
            @Override
            public int compare(Row r1, Row r2) {
                return compareRow(r1, r2, tbl.getColumnCount());
            }
        });
    }

    public static int compareRow(Row row1, Row row2, int colcount) {
        for (int i = 0; i < colcount - 1; i++) {
            Key key1 = (Key)row1.getValue(i);
            Key key2 = (Key)row2.getValue(i);

            if (key1 == null && key2 == null) {
                return 0;
            }

            if (key1 == null) {
                return -1;
            }

            if (key2 == null) {
                return 1;
            }

            // compare the values
            int res = key1.getVariable().compareValues(key1, key2);

            if (res != 0) {
                return res;
            }
        }

        return 0;
    }
}
