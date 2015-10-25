package org.msh.etbm.services.admin.admunits;

import java.util.UUID;

/**
 * Created by rmemoria on 24/10/15.
 */
public class CountryStructureData {
    private UUID id;
    private String name;
    private int level;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
