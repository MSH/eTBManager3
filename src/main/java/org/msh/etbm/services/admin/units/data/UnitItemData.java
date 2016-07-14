package org.msh.etbm.services.admin.units.data;

import org.msh.etbm.services.admin.units.UnitType;

import java.util.UUID;

/**
 * Simple DTO object to return from the service information about an unit
 * <p>
 * Created by rmemoria on 28/10/15.
 */
public class UnitItemData {
    private UnitType type;

    private UUID id;

    private String name;

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

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

    @Override
    public String toString() {
        return "UnitItemData{" +
                "type=" + type +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
