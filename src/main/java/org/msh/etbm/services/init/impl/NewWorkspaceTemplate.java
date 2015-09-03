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
    private AdministrativeUnit[] regions;
    private AdministrativeUnit[] cities;
    private Tbunit healthUnit;
    private UserProfile[] profiles;

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

    public AdministrativeUnit[] getRegions() {
        return regions;
    }

    public void setRegions(AdministrativeUnit[] regions) {
        this.regions = regions;
    }

    public AdministrativeUnit[] getCities() {
        return cities;
    }

    public void setCities(AdministrativeUnit[] cities) {
        this.cities = cities;
    }

    public Tbunit getHealthUnit() {
        return healthUnit;
    }

    public void setHealthUnit(Tbunit healthUnit) {
        this.healthUnit = healthUnit;
    }

    public UserProfile[] getProfiles() {
        return profiles;
    }

    public void setProfiles(UserProfile[] profiles) {
        this.profiles = profiles;
    }
}
