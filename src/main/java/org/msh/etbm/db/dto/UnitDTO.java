package org.msh.etbm.db.dto;

import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.units.UnitType;

import java.util.UUID;

/**
 * Created by rmemoria on 5/10/15.
 */
public class UnitDTO {
    private UUID id;
    private String name;
    private UnitType type;
    private AdminUnitSeries adminUnit;

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

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public AdminUnitSeries getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitSeries adminUnit) {
        this.adminUnit = adminUnit;
    }
}
