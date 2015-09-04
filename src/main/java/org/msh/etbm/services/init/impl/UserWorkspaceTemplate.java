package org.msh.etbm.services.init.impl;

import org.msh.etbm.db.entities.User;
import org.msh.etbm.db.enums.UserView;

/**
 * Template about a user to be registered in the system when creating a new workspace
 * Created by rmemoria on 4/9/15.
 */
public class UserWorkspaceTemplate {
    private User user;
    private String unitName;
    private String[] profiles;
    private UserView userView;
    private boolean playOtherUnits;
    private String adminUnitName;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String[] getProfiles() {
        return profiles;
    }

    public void setProfiles(String[] profiles) {
        this.profiles = profiles;
    }

    public UserView getUserView() {
        return userView;
    }

    public void setUserView(UserView userView) {
        this.userView = userView;
    }

    public boolean isPlayOtherUnits() {
        return playOtherUnits;
    }

    public void setPlayOtherUnits(boolean playOtherUnits) {
        this.playOtherUnits = playOtherUnits;
    }

    public String getAdminUnitName() {
        return adminUnitName;
    }

    public void setAdminUnitName(String adminUnitName) {
        this.adminUnitName = adminUnitName;
    }
}
