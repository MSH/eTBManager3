package org.msh.etbm.services.admin.usersws.data;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.enums.UserState;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;
import org.msh.etbm.services.admin.units.data.UnitItemData;


/**
 * Default data to be returned from a user in the user workspace service
 * Created by rmemoria on 26/1/16.
 */
public class UserWsData extends SynchronizableItem {

    private UnitItemData unit;
    private AdminUnitSeries adminUnit;

    private UserState state;
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

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
