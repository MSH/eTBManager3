package org.msh.etbm.commons.models.data.fields;

import org.msh.etbm.commons.date.YearMonthPeriod;

/**
 * Created by rmemoria on 6/12/16.
 */
public class YearMonthPeriodField extends Field {

    /**
     * The period
     */
    private YearMonthPeriod period;

    public YearMonthPeriod getPeriod() {
        return period;
    }

    public void setPeriod(YearMonthPeriod period) {
        this.period = period;
    }
}
