package org.msh.etbm.services.cases.reports;

import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;
import org.msh.etbm.services.cases.indicators.CaseIndicatorFormData;
import org.msh.etbm.services.cases.indicators.CaseIndicatorResponse;

/**
 * Created by rmemoria on 20/10/16.
 */
public class CaseIndicatorData extends CaseIndicatorFormData {

    private IndicatorData data;

    public IndicatorData getData() {
        return data;
    }

    public void setData(IndicatorData data) {
        this.data = data;
    }
}
