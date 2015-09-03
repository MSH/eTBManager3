package org.msh.etbm.services.init.impl;

/**
 * Store information about an administrative unit that comes from a json template
 *
 * Created by rmemoria on 3/9/15.
 */
public class AdminUnitTemplate {
    private String name;
    private String parent;
    private String countryStructure;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCountryStructure() {
        return countryStructure;
    }

    public void setCountryStructure(String countryStructure) {
        this.countryStructure = countryStructure;
    }
}
