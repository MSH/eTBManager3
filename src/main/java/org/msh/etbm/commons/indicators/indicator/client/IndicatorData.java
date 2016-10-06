package org.msh.etbm.commons.indicators.indicator.client;

import java.util.List;

/**
 * Indicator data ready to be serialized to a client request
 *
 * Created by rmemoria on 1/10/16.
 */
public class IndicatorData {

    private DimensionData columns;
    private DimensionData rows;
    private List<List> values;

    public DimensionData getColumns() {
        return columns;
    }

    public void setColumns(DimensionData columns) {
        this.columns = columns;
    }

    public DimensionData getRows() {
        return rows;
    }

    public void setRows(DimensionData rows) {
        this.rows = rows;
    }

    public List<List> getValues() {
        return values;
    }

    public void setValues(List<List> values) {
        this.values = values;
    }
}
