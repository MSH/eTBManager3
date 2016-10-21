package org.msh.etbm.services.cases.reports;

import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.services.cases.indicators.CaseIndicatorFormData;

/**
 * Indicator data returned to the client. It is wrapped inside a {@link ReportExecResult} instance
 *
 * Created by rmemoria on 20/10/16.
 */
public class ReportIndicatorExec {
    /**
     * The indicator data
     */
    private IndicatorData data;

    /**
     * The indicator variables, filters, title, etc.
     */
    private CaseIndicatorFormData schema;


    public IndicatorData getData() {
        return data;
    }

    public void setData(IndicatorData data) {
        this.data = data;
    }

    public CaseIndicatorFormData getSchema() {
        return schema;
    }

    public void setSchema(CaseIndicatorFormData schema) {
        this.schema = schema;
    }
}
