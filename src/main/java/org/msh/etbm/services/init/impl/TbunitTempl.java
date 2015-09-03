package org.msh.etbm.services.init.impl;

import org.msh.etbm.db.entities.Tbunit;

/**
 * Store information about a TB unit that comes from a json template
 *
 * Created by rmemoria on 3/9/15.
 */
public class TbunitTempl extends Tbunit {
    private String adminUnit;

    public String getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(String adminUnit) {
        this.adminUnit = adminUnit;
    }
}
