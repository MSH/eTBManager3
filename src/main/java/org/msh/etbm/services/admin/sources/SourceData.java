package org.msh.etbm.services.admin.sources;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Information about a source to be returned under a request of source information
 *
 * Created by rmemoria on 11/11/15.
 */
public class SourceData {

    private String name;
    private String shortName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }
}
