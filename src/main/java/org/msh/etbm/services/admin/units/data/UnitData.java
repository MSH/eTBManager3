package org.msh.etbm.services.admin.units.data;


import org.msh.etbm.services.admin.admunits.data.AdminUnitData;

/**
 * Created by rmemoria on 1/11/15.
 */
public class UnitData extends UnitItemData {
    private boolean active;
    private AdminUnitData adminUnit;

    public AdminUnitData getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitData adminUnit) {
        this.adminUnit = adminUnit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
