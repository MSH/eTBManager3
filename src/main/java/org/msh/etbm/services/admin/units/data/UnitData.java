package org.msh.etbm.services.admin.units.data;

import java.util.UUID;

/**
 * Created by rmemoria on 1/11/15.
 */
public class UnitData extends UnitItemData {
    private UUID adminUnitId;
    private String adminUnitName;

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public String getAdminUnitName() {
        return adminUnitName;
    }

    public void setAdminUnitName(String adminUnitName) {
        this.adminUnitName = adminUnitName;
    }
}
