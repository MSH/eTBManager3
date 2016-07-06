package org.msh.etbm.services.admin.errorlogrep;

import org.msh.etbm.commons.entities.query.EntityQueryParams;

import java.util.Date;

/**
 * Created by msantos on 05/7/16.
 */
public class ErrorLogRepQueryParams extends EntityQueryParams {

    Date iniDate;
    Date endDate;
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

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }
}
