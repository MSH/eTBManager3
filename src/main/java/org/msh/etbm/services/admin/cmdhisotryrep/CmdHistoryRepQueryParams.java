package org.msh.etbm.services.admin.cmdhisotryrep;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rmemoria on 10/3/16.
 */
public class CmdHistoryRepQueryParams extends EntityQueryParams {

    Date iniDate;
    Date endDate;
    UUID userId;
    String type;
    UUID adminUnitId;
    String searchKey;

    public Date getIniDate() {
        return iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getAdminUnitId() {
        return adminUnitId;
    }

    public void setAdminUnitId(UUID adminUnitId) {
        this.adminUnitId = adminUnitId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
