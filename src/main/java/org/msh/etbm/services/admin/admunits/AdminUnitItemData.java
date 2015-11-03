package org.msh.etbm.services.admin.admunits;

import java.util.UUID;

/**
 * Store simple information about an administrative unit
 * Created by rmemoria on 1/11/15.
 */
public class AdminUnitItemData {
    private UUID id;
    private String name;

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
}
