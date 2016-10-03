package org.msh.etbm.commons.indicators.variables;

import org.msh.etbm.commons.indicators.DetailedIndicatorRequest;
import org.msh.etbm.commons.indicators.IndicatorGenerator;
import org.msh.etbm.commons.indicators.query.DataTableQuery;

/**
 * Detailed result from an indicator request made by
 * {@link IndicatorGenerator#getDetailedReport(DetailedIndicatorRequest)}
 *
 * Created by rmemoria on 10/9/16.
 */
public class DetailedIndicatorResponse {
    private DataTableQuery table;
    private long count;

    public DetailedIndicatorResponse(DataTableQuery table, long count) {
        this.table = table;
        this.count = count;
    }

    public DataTableQuery getTable() {
        return table;
    }

    public long getCount() {
        return count;
    }
}
