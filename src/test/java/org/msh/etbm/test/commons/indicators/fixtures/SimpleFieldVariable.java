package org.msh.etbm.test.commons.indicators.fixtures;

import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.sqlquery.QueryDefs;

/**
 * Implementation of {@link Variable} representing a single field in a table (just for testing)
 *
 * Created by rmemoria on 10/9/16.
 */
public class SimpleFieldVariable implements Variable {

    private String id;
    private String label;
    private String fieldName;

    public SimpleFieldVariable(String id, String label, String fieldName) {
        this.id = id;
        this.label = label;
        this.fieldName = fieldName;
    }

    @Override
    public void prepareVariableQuery(QueryDefs def, int iteration) {
        def.select(fieldName);
    }

    @Override
    public Object createKey(Object values) {
        return values;
    }

    @Override
    public String getDisplayText(Object key) {
        return key != null ? key.toString() : "-";
    }

    @Override
    public int compareValues(Object val1, Object val2) {
        return 0;
    }

    @Override
    public int compareGroupValues(Object val1, Object val2) {
        return 0;
    }

    @Override
    public Object[] getDomain() {
        return new Object[0];
    }

    @Override
    public boolean isGrouped() {
        return false;
    }

    @Override
    public Object createGroupKey(Object values) {
        return values;
    }

    @Override
    public String getGroupDisplayText(Object key) {
        return key != null ? key.toString() : "-";
    }

    @Override
    public int getIteractionCount() {
        return 0;
    }

    @Override
    public boolean isTotalEnabled() {
        return false;
    }

    @Override
    public Object getUnitType() {
        return "units";
    }

    @Override
    public String getUnitTypeLabel() {
        return "Units";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
