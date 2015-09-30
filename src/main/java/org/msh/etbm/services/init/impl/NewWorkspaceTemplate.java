package org.msh.etbm.services.init.impl;

import org.msh.etbm.db.entities.CountryStructure;
import org.msh.etbm.db.entities.Workspace;

/**
 * Template class that will be filled by the json file located in the resources folder
 *
 * Created by rmemoria on 2/9/15.
 */
public class NewWorkspaceTemplate {
    private Workspace workspace;
    private UserWorkspaceTemplate userWorkspace;
    private CountryStructure[] countryStructures;
    private AdminUnitTemplate[] adminUnits;
    private TbunitTemplate[] tbunits;
    private UserProfileTemplate[] profiles;

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public UserWorkspaceTemplate getUser() {
        return userWorkspace;
    }

    public void setUserWorkspace(UserWorkspaceTemplate user) {
        this.userWorkspace = user;
    }

    public CountryStructure[] getCountryStructures() {
        return countryStructures;
    }

    public void setCountryStructures(CountryStructure[] countryStructures) {
        this.countryStructures = countryStructures;
    }

    public UserProfileTemplate[] getProfiles() {
        return profiles;
    }

    public void setProfiles(UserProfileTemplate[] profiles) {
        this.profiles = profiles;
    }

    public AdminUnitTemplate[] getAdminUnits() {
        return adminUnits;
    }

    public void setAdminUnits(AdminUnitTemplate[] adminUnits) {
        this.adminUnits = adminUnits;
    }

    public TbunitTemplate[] getTbunits() {
        return tbunits;
    }

    public void setTbunits(TbunitTemplate[] tbunits) {
        this.tbunits = tbunits;
    }
}
