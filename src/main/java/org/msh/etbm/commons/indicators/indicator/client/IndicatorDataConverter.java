package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.indicators.indicator.DataTableIndicator;
import org.msh.etbm.commons.indicators.indicator.HeaderRow;
import org.msh.etbm.commons.indicators.indicator.IndicatorColumn;
import org.msh.etbm.commons.indicators.indicator.IndicatorRow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rmemoria on 1/10/16.
 */
public class IndicatorDataConverter {

    public IndicatorData convertFromDataTableIndicator(DataTableIndicator tbl) {
        IndicatorData res = new IndicatorData();

        // create the rows with keys and values
        RowData rowData = createRows(tbl);
        res.setRows(rowData);

        ColumnData cols = createColumns(tbl);
        res.setColumns(cols);

        return res;
    }

    protected ColumnData createColumns(DataTableIndicator tbl) {
        ColumnData cols = new ColumnData();

        List<Item<String>> lst = new ArrayList<>();
        HeaderRow hr = tbl.getHeaderRow(tbl.getHeaderRowsCount() - 1);
        for (IndicatorColumn col: hr.getColumns()) {
            String key = col.getKey() != null ? col.getKey().toString() : null;
            String title = col.getTitle();
            lst.add(new Item<String>(key, title));
        }
        cols.setKeys(lst);
        cols.setLevels(tbl.getHeaderRowsCount());
        cols.setVariables(tbl.getRowVariables().stream()
            .map(v -> new VariableData(v.getId(), v.getLabel(), v.isGrouped(), v.isTotalEnabled()))
            .collect(Collectors.toList()));

        return cols;
    }

    protected RowData createRows(DataTableIndicator tbl) {
        RowData rowData = new RowData();
        List<KeyValues> keyValues = new ArrayList<>();

        for (IndicatorRow row: tbl.getIndicatorRows()) {
            String key = row.getKey() != null ? row.getKey().toString() : null;
            String title = row.getTitle();
            List values = row.getValues();

            KeyValues kv = new KeyValues();
            kv.setId(key);
            kv.setName(title);
            kv.setValues(values.toArray());

            keyValues.add(kv);
        }

        rowData.setKeyValues(keyValues);
        List<VariableData> vars = tbl.getRowVariables().stream()
                .map(v -> new VariableData(v.getId(), v.getLabel(), v.isGrouped(), v.isTotalEnabled()))
                .collect(Collectors.toList());

        rowData.setVariables(vars);

        return rowData;
    }
}
