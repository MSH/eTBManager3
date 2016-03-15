package org.msh.etbm.services.admin.sessionreport;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.Date;
import java.util.UUID;

/**
 * Created by rmemoria on 10/3/16.
 */
public class UserSessionRepQueryParams extends EntityQueryParams {

    Date iniDate;
    Date endDate;
    UUID userId;

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
}
