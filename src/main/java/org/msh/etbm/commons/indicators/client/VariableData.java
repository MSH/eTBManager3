package org.msh.etbm.commons.indicators.client;

import org.msh.etbm.commons.Item;

/**
 * Store information about a variable used to generate an indicator
 *
 * Created by rmemoria on 30/9/16.
 */
public class VariableData extends Item<String> {
    private boolean grouped;
    private boolean total;

    public boolean isGrouped() {
        return grouped;
    }

    public void setGrouped(boolean grouped) {
        this.grouped = grouped;
    }

    public boolean isTotal() {
        return total;
    }

    public void setTotal(boolean total) {
        this.total = total;
    }
}
