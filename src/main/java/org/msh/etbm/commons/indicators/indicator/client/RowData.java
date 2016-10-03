package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.Item;

import java.util.List;

/**
 * Created by rmemoria on 1/10/16.
 */
public class RowData {
    private List<VariableData> variables;

    private List<KeyValues> keyValues;

    private int levels;


    public List<VariableData> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableData> variables) {
        this.variables = variables;
    }

    public List<KeyValues> getKeyValues() {
        return keyValues;
    }

    public void setKeyValues(List<KeyValues> keyValues) {
        this.keyValues = keyValues;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }
}
