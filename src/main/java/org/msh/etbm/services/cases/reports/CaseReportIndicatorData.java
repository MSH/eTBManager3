package org.msh.etbm.services.cases.reports;

import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.services.cases.indicators.CaseIndicatorFormData;

/**
 * Created by rmemoria on 20/10/16.
 */
public class CaseReportIndicatorData {

    private CaseIndicatorFormData schema;
    private IndicatorData data;

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
