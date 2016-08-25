package org.msh.etbm.services.session.usersession;

import org.msh.etbm.commons.SynchronizableItem;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.db.enums.UserView;
import org.msh.etbm.services.admin.admunits.data.AdminUnitData;

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
    private UUID userWorkspaceId;
    private UUID workspaceId;
    private String workspaceName;

    // information about the user unit
    private UUID unitId;
    private String unitName;

    // the administrative unit of the unit
    private AdminUnitData adminUnit;

    private UserView view;

    private boolean playOtherUnits;

    // true if user is administrator
    private boolean administrator;

    // list of permissions granted to the user
    private List<String> permissions;

    private NameComposition patientNameComposition;


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

    public UUID getUserWorkspaceId() {
        return userWorkspaceId;
    }

    public void setUserWorkspaceId(UUID userWorkspaceId) {
        this.userWorkspaceId = userWorkspaceId;
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

    public AdminUnitData getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdminUnitData adminUnit) {
        this.adminUnit = adminUnit;
    }

    public NameComposition getPatientNameComposition() {
        return patientNameComposition;
    }

    public void setPatientNameComposition(NameComposition patientNameComposition) {
        this.patientNameComposition = patientNameComposition;
    }

    public UUID getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(UUID workspaceId) {
        this.workspaceId = workspaceId;
    }
}
