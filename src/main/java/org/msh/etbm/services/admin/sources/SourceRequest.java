package org.msh.etbm.services.admin.sources;

import java.util.Optional;

/**
 * Request object to save or update a new source
 * Created by rmemoria on 11/11/15.
 */
public class SourceRequest {

    public static final String ORDERBY_NAME = "name";
    public static final String ORDERBY_SHORTNAME = "shortName";

    private Optional<String> name;
    private Optional<String> shortName;
    private Optional<String> customId;

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
}
