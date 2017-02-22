package org.msh.etbm.services.cases.indicators;

import org.msh.etbm.commons.indicators.indicator.client.IndicatorData;

/**
 * Response with information about the indicator
 * Created by rmemoria on 10/9/16.
 */
public class CaseIndicatorResponse {
    /**
     * Store data about the indicator
     */
    private IndicatorData indicator;

    public IndicatorData getIndicator() {
        return indicator;
    }

    public void setIndicator(IndicatorData indicator) {
        this.indicator = indicator;
    }
}
