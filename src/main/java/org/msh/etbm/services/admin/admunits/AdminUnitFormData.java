package org.msh.etbm.services.admin.admunits;

import java.util.Optional;
import java.util.UUID;

/**
 * Created by rmemoria on 21/10/15.
 */
public class AdminUnitFormData {

    private Optional<String> name;

    private Optional<UUID> parentId;

    private Optional<UUID> csId;

    private Optional<String> customId;


    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<UUID> getParentId() {
        return parentId;
    }

    public void setParentId(Optional<UUID> parentId) {
        this.parentId = parentId;
    }

    public Optional<UUID> getCsId() {
        return csId;
    }

    public void setCsId(Optional<UUID> csId) {
        this.csId = csId;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }
}
