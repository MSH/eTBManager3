package org.msh.etbm.services.admin.units.forms;

import org.msh.etbm.services.admin.admunits.AdminUnitItemData;
import org.msh.etbm.services.admin.units.data.UnitItemData;

import java.util.List;
import java.util.UUID;

/**
 * Data to be sent to a client UI in order to initialize a unit field for editing
 *
 * Created by rmemoria on 18/1/16.
 */
public class UnitFormResponse {

    /**
     * The workspace in use to fill the results (or null if it is the current workspace)
     */
    private UUID workspaceId;

    /**
     * List of administrative units to fill a select box
     */
    private List<AdminUnitItemData> adminUnits;

    /**
     * The selected administrative unit
     */
    private UUID adminUnitId;

    /**
     * List of administrative units to fill a select box
     */
    private List<UnitItemData> units;

    public UnitFormResponse(List<AdminUnitItemData> adminUnits, List<UnitItemData> units) {
        this.adminUnits = adminUnits;
        this.units = units;
    }

    public UnitFormResponse() {
        super();
    }

    public List<AdminUnitItemData> getAdminUnits() {
        return adminUnits;
    }

    public void setAdminUnits(List<AdminUnitItemData> adminUnits) {
        this.adminUnits = adminUnits;
    }

    public List<UnitItemData> getUnits() {
        return units;
    }

    public void setUnits(List<UnitItemData> units) {
        this.units = units;
    }

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }
}
