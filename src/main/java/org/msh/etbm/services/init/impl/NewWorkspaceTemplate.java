package org.msh.etbm.services.init.impl;

import org.msh.etbm.db.entities.*;

/**
 * Template class that will be filled by the json file located in the resources
 *
 * Created by rmemoria on 2/9/15.
 */
public class NewWorkspaceTemplate {
    private Workspace workspace;
    private User user;
    private CountryStructure[] countryStructures;
    private AdminUnitTemplate[] adminUnits;
    private TbunitTempl[] tbunits;
    private UserProfileInfo[] profiles;

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public CountryStructure[] getCountryStructures() {
        return countryStructures;
    }

    public void setCountryStructures(CountryStructure[] countryStructures) {
        this.countryStructures = countryStructures;
    }

    public UserProfileInfo[] getProfiles() {
        return profiles;
    }

    public void setProfiles(UserProfileInfo[] profiles) {
        this.profiles = profiles;
    }

    public AdminUnitTemplate[] getAdminUnits() {
        return adminUnits;
    }

    public void setAdminUnits(AdminUnitTemplate[] adminUnits) {
        this.adminUnits = adminUnits;
    }

    public TbunitTempl[] getTbunits() {
        return tbunits;
    }

    public void setTbunits(TbunitTempl[] tbunits) {
        this.tbunits = tbunits;
    }
}
