package org.msh.etbm.services.admin.units.data;

import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;

/**
 * Created by rmemoria on 1/11/15.
 */
public class UnitData extends UnitItemData {
    private boolean active;
    private AdminUnitSeries adminUnit;

    public AdminUnitSeries getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitSeries adminUnit) {
        this.adminUnit = adminUnit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
