package org.msh.etbm.services.admin.admunits;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 *
 * Created by rmemoria on 21/10/15.
 */
public class AdminUnitRequest {

    /**
     * Available profiles
     */
    public static final String QUERY_PROFILE_ITEM = "item";
    public static final String QUERY_PROFILE_DEFAULT = "default";
    public static final String QUERY_PROFILE_DETAILED = "detailed";

    @NotNull
    private String name;

    private UUID parentId;

    @NotNull
    private UUID csId;

    private String customId;

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

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }
}
