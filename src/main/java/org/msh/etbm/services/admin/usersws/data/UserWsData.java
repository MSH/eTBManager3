package org.msh.etbm.services.admin.usersws.data;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.units.data.UnitItemData;


/**
 * Default data to be returned from a user in the user workspace service
 * Created by rmemoria on 26/1/16.
 */
public class UserWsData extends SynchronizableItem {

    private UnitItemData unit;
    private AdminUnitSeries adminUnit;

    private boolean active;
    private boolean emailConfirmed;
    private boolean passwordExpired;
    private boolean administrator;


    public UnitItemData getUnit() {
        return unit;
    }

    public void setUnit(UnitItemData unit) {
        this.unit = unit;
    }

    public AdminUnitSeries getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitSeries adminUnit) {
        this.adminUnit = adminUnit;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }

    public void setEmailConfirmed(boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
    }

    public boolean isPasswordExpired() {
        return passwordExpired;
    }

    public void setPasswordExpired(boolean passwordExpired) {
        this.passwordExpired = passwordExpired;
    }
}
