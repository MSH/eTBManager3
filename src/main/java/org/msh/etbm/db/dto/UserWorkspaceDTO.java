package org.msh.etbm.db.dto;

import org.msh.etbm.db.enums.UserView;

import java.util.UUID;

/**
 * Created by rmemoria on 5/10/15.
 */
public class UserWorkspaceDTO {
    private UUID id;
    private UserDTO user;
    private UnitDTO unit;
    private AdministrativeUnitDTO adminUnit;
    private WorkspaceDTO workspace;
    private boolean administrator;
    private boolean playOtherUnits;
    private UserView view;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public WorkspaceDTO getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceDTO workspace) {
        this.workspace = workspace;
    }

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }

    public boolean isPlayOtherUnits() {
        return playOtherUnits;
    }

    public void setPlayOtherUnits(boolean playOtherUnits) {
        this.playOtherUnits = playOtherUnits;
    }

    public UnitDTO getUnit() {
        return unit;
    }

    public void setUnit(UnitDTO unit) {
        this.unit = unit;
    }

    public AdministrativeUnitDTO getAdminUnit() {
        return adminUnit;
    }

    public void setAdminUnit(AdministrativeUnitDTO adminUnit) {
        this.adminUnit = adminUnit;
    }

    public UserView getView() {
        return view;
    }

    public void setView(UserView view) {
        this.view = view;
    }
}
