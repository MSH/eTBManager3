package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.Item;

/**
 * Created by rmemoria on 1/10/16.
 */
public class KeyValues extends Item<String> {

    private Object[] values;

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }
}
