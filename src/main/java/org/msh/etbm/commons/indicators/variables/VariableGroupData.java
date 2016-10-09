package org.msh.etbm.commons.indicators.variables;

import org.msh.etbm.commons.indicators.variables.VariableData;

import java.util.List;

/**
 * Created by rmemoria on 7/10/16.
 */
public class VariableGroupData {

    private String label;

    private List<VariableData> variables;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<VariableData> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableData> variables) {
        this.variables = variables;
    }
}
