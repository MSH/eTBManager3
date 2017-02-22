package org.msh.etbm.services.cases.summary;

import org.msh.etbm.commons.Item;

/**
 * Store information about a summary item
 *
 * Created by rmemoria on 17/9/16.
 */
public class SummaryReportData extends Item<String> {

    /**
     * The number of cases for the summary item
     */
    private long count;


    public SummaryReportData(String id, String name, long count) {
        super(id, name);
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
