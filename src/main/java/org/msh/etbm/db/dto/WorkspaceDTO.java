package org.msh.etbm.db.dto;

import java.util.UUID;

/**
 * Created by rmemoria on 5/10/15.
 */
public class WorkspaceDTO {
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
