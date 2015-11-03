package org.msh.etbm.services.admin.admunits;

/**
 * Detailed data about an administrative unit to be returned by the admin unit service
 *
 * Created by rmemoria on 31/10/15.
 */
public class AdminUnitDetailedData extends AdminUnitData {

    private String code;

    private String customId;

    private int unitsCount;

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

    public int getUnitsCount() {
        return unitsCount;
    }

    public void setUnitsCount(int unitsCount) {
        this.unitsCount = unitsCount;
    }
}
