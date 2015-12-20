package org.msh.etbm.services.usersession;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.admunits.parents.AdminUnitSeries;

import java.util.List;
import java.util.UUID;

/**
 * Response data to be sent to a client containing information about a user session
 * Created by rmemoria on 21/11/15.
 */
public class UserSessionResponse {
    // information about the user
    private String userName;
    private UUID userId;

    // information about the workspace
    private UUID workspaceId;
    private String workspaceName;

    // information about the user unit
    private UUID unitId;
    private String unitName;

    // the administrative unit of the unit
    private AdminUnitSeries adminUnit;

    private UserView view;

    private boolean playOtherUnits;

    // true if user is administrator
    private boolean administrator;

    // list of permissions granted to the user
    private List<String> permissions;

    // list of user workspaces
    private List<SynchronizableItem> workspaces;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getWorkspaceName() {
        return workspaceName;
    }

    public void setWorkspaceName(String workspaceName) {
        this.workspaceName = workspaceName;
    }

    public UUID getUnitId() {
        return unitId;
    }

    public void setUnitId(UUID unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public UserView getView() {
        return view;
    }

    public void setView(UserView view) {
        this.view = view;
    }

    public boolean isPlayOtherUnits() {
        return playOtherUnits;
    }

    public void setPlayOtherUnits(boolean playOtherUnits) {
        this.playOtherUnits = playOtherUnits;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<SynchronizableItem> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(List<SynchronizableItem> workspaces) {
        this.workspaces = workspaces;
    }

    public AdminUnitSeries getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitSeries adminUnit) {
        this.adminUnit = adminUnit;
    }
}
