package org.msh.etbm.services.init.impl;

import org.msh.etbm.db.entities.Tbunit;

/**
 * Store information about a TB unit that comes from a json template
 *
 * Created by rmemoria on 3/9/15.
 */
public class TbunitTemplate extends Tbunit {
    private String adminUnitName;

    public String getAdminUnitName() {
        return adminUnitName;
    }

    public void setAdminUnitName(String adminUnit) {
        this.adminUnitName = adminUnit;
    }
}
