package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.Item;

import java.util.List;

/**
 * Created by rmemoria on 1/10/16.
 */
public class ColumnData {
    private List<VariableData> variables;

    private List<Item<String>> keys;

    private int levels;


    public List<VariableData> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableData> variables) {
        this.variables = variables;
    }

    public List<Item<String>> getKeys() {
        return keys;
    }

    public void setKeys(List<Item<String>> keys) {
        this.keys = keys;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }
}
