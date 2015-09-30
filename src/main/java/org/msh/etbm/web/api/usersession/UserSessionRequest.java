package org.msh.etbm.web.api.usersession;

import org.msh.etbm.db.enums.UserView;

/**
 * Store user session information requested by the client
 *
 * Created by rmemoria on 30/9/15.
 */
public class UserSessionRequest {

    private UserInfo user;
    private WorkspaceInfo workspace;
    private AdminUnitInfo adminUnit;
    private String[] perms;
    private boolean playOtherUnits;
    private UserView view;

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public WorkspaceInfo getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceInfo workspace) {
        this.workspace = workspace;
    }

    public String[] getPerms() {
        return perms;
    }

    public void setPerms(String[] perms) {
        this.perms = perms;
    }

    public boolean isPlayOtherUnits() {
        return playOtherUnits;
    }

    public void setPlayOtherUnits(boolean playOtherUnits) {
        this.playOtherUnits = playOtherUnits;
    }

    public UserView getView() {
        return view;
    }

    public void setView(UserView view) {
        this.view = view;
    }

    public AdminUnitInfo getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitInfo adminUnit) {
        this.adminUnit = adminUnit;
    }
}
