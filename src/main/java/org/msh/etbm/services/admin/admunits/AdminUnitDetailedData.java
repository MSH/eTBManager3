package org.msh.etbm.services.admin.admunits;

import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;

import java.util.UUID;

/**
 * Detailed data about an administrative unit to be returned by the admin unit service
 *
 * Created by rmemoria on 31/10/15.
 */
public class AdminUnitDetailedData extends AdminUnitItemData {
    private UUID csId;
    private String csName;

    private String code;

    private String customId;

    private AdminUnitSeries parents;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public AdminUnitSeries getParents() {
        return parents;
    }

    public void setParents(AdminUnitSeries parents) {
        this.parents = parents;
    }

    public UUID getCsId() {
        return csId;
    }

    public void setCsId(UUID csId) {
        this.csId = csId;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String csName) {
        this.csName = csName;
    }
}
