package org.msh.etbm.web.api.usersession;

import java.util.UUID;

/**
 * Created by rmemoria on 30/9/15.
 */
public class AdminUnitInfo {
    private UUID id;
    private String name;
    private AdminUnitInfo parent;

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

    public AdminUnitInfo getParent() {
        return parent;
    }

    public void setParent(AdminUnitInfo parent) {
        this.parent = parent;
    }
}
