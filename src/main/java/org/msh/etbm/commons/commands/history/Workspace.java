package org.msh.etbm.commons.commands.history;

import java.util.UUID;

/**
 * Created by rmemoria on 7/10/15.
 */
public class Workspace {
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
