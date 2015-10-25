package org.msh.etbm.services.admin.admunits;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 *
 * Created by rmemoria on 21/10/15.
 */
public class AdminUnitRequest {
    @NotNull
    private String name;

    private UUID parentId;

    @NotNull
    private UUID csId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public UUID getCsId() {
        return csId;
    }

    public void setCsId(UUID csId) {
        this.csId = csId;
    }
}
