package org.msh.etbm.commons.indicators.indicator.client;

import org.msh.etbm.commons.indicators.variables.VariableData;

import java.util.List;
import java.util.Map;

/**
 * Data of a dimension of the indicator table (row or column)
 *
 * Created by rmemoria on 5/10/16.
 */
public class DimensionData {

    /**
     * Descriptors containing the columns by its key ids
     */
    private List<Map<String, String>> descriptors;
    /**
     * The key of each column
     */
    private List<Object[]> keys;

    /**
     * The variables that were used in this dimension
     */
    private List<VariableData> variables;


    public List<Map<String, String>> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<Map<String, String>> descriptors) {
        this.descriptors = descriptors;
    }

    public List<Object[]> getKeys() {
        return keys;
    }

    public void setKeys(List<Object[]> keys) {
        this.keys = keys;
    }

    public List<VariableData> getVariables() {
        return variables;
    }

    public void setVariables(List<VariableData> variables) {
        this.variables = variables;
    }
}
