package org.msh.etbm.services.usersession;

import org.msh.etbm.db.entities.AdministrativeUnit;

import java.util.UUID;

/**
 * Created by rmemoria on 5/10/15.
 */
public class AdministrativeUnitDTO {
    private UUID id;
    private String name;
    private String csName;
    private AdministrativeUnitDTO parent;

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

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }

    public AdministrativeUnitDTO getParent() {
        return parent;
    }

    public void setParent(AdministrativeUnitDTO parent) {
        this.parent = parent;
    }
}
