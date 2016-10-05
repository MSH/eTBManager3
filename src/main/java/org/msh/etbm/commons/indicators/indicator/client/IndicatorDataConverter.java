package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;

/**
 * Created by rmemoria on 1/10/16.
 */
public class IndicatorDataConverter {

    public IndicatorData convertFromDataTableIndicator(IndicatorDataTable tbl) {
        IndicatorData res = new IndicatorData();

        // create the rows with keys and values
        RowData rowData = createRows(tbl);
        res.setRows(rowData);

        ColumnData cols = createColumns(tbl);
        res.setColumns(cols);

        return res;
    }

    protected ColumnData createColumns(IndicatorDataTable tbl) {
        ColumnData cols = new ColumnData();
        cols.setKeys(tbl.getColumnKeyDescriptors().getGroups());

        return cols;
    }

    protected RowData createRows(IndicatorDataTable tbl) {
        RowData rowData = new RowData();
        rowData.setKeys(tbl.getRowKeyDescriptors().getGroups());

        return rowData;
    }
}
