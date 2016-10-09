package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.indicators.variables.VariableData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by rmemoria on 1/10/16.
 */
public class IndicatorDataConverter {

    public static IndicatorData convertFromDataTableIndicator(IndicatorDataTable tbl) {
        IndicatorData res = new IndicatorData();

        // create the rows with keys and values
        res.setRows(createRows(tbl));
        res.setColumns(createColumns(tbl));

        List lst = new ArrayList();
        for (int i = 0; i < tbl.getRowCount(); i++) {
            lst.add(tbl.getRowValues(i));
        }
        res.setValues(lst);

        return res;
    }

    protected static DimensionData createColumns(IndicatorDataTable tbl) {
        DimensionData col = new DimensionData();

        col.setDescriptors(tbl.getColumnKeyDescriptors());
        col.setKeys(tbl.getColumnKeys());
        col.setVariables(convertVariables(tbl.getColumnVariables()));

        return col;
    }

    protected static DimensionData createRows(IndicatorDataTable tbl) {
        DimensionData row = new DimensionData();

        row.setDescriptors(tbl.getRowKeyDescriptors());
        row.setKeys(tbl.getRowKeys());
        row.setVariables(convertVariables(tbl.getRowVariables()));

        return row;
    }

    /**
     * Convert a list of variables to a list of {@link VariableData} instances
     * @param lst
     * @return
     */
    protected static List<VariableData> convertVariables(List<Variable> lst) {
        if (lst == null) {
            return null;
        }

        List<VariableData> res = lst.stream()
                .map(v -> new VariableData(v.getId(),
                        v.getLabel(),
                        v.getVariableOptions().isGrouped(),
                        v.getVariableOptions().isTotalEnabled()))
                .collect(Collectors.toList());

        return res;
    }
}
