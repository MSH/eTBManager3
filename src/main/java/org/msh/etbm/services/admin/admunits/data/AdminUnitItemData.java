package org.msh.etbm.services.admin.admunits.data;

import java.util.UUID;

/**
 * Store simple information about an administrative unit
 * Created by rmemoria on 1/11/15.
 */
public class AdminUnitItemData {
    /**
     * Admin unit unique ID
     */
    private UUID id;

    /**
     * Name of the administrative unit
     */
    private String name;

    /**
     * Number of child admin units under this admin unit
     */
    private int unitsCount;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnitsCount() {
        return unitsCount;
    }

    public void setUnitsCount(int unitCount) {
        this.unitsCount = unitCount;
    }
}
