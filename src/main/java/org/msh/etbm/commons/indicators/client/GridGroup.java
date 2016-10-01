package org.msh.etbm.commons.indicators.client;

import org.msh.etbm.commons.Item;

import java.util.List;

/**
 * The
 * Created by rmemoria on 30/9/16.
 */
public class GridGroup {

    private List<VariableData> variables;

    private List<GridGroup> groups;

    public List<VariableData> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableData> variables) {
        this.variables = variables;
    }

    public List<GridGroup> getGroups() {
        return groups;
    }

    public void setGroups(List<GridGroup> groups) {
        this.groups = groups;
    }
}
