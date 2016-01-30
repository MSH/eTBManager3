package org.msh.etbm.services.admin.sources;

import java.util.Optional;

/**
 * Request object to save or update a new source
 * Created by rmemoria on 11/11/15.
 */
public class SourceFormData {

    private Optional<String> name;
    private Optional<String> shortName;
    private Optional<String> customId;
    private Optional<Boolean> active;

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getShortName() {
        return shortName;
    }

    public void setShortName(Optional<String> shortName) {
        this.shortName = shortName;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }

    public Optional<Boolean> getActive() {
        return active;
    }

    public void setActive(Optional<Boolean> active) {
        this.active = active;
    }
}
