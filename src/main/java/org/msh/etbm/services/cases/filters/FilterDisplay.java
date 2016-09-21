package org.msh.etbm.services.cases.filters;

/**
 * Contain information about a filter and its value to be displayed
 * Created by rmemoria on 21/9/16.
 */
public class FilterDisplay {

    /**
     * The name of the filter
     */
    private String name;

    /**
     * A string representation of the filter values to be displayed to the user
     */
    private String value;

    public FilterDisplay() {

    }

    public FilterDisplay(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
