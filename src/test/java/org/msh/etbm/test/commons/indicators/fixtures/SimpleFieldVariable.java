package org.msh.etbm.test.commons.indicators.fixtures;

import org.msh.etbm.commons.Item;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.indicators.variables.VariableOptions;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.springframework.context.ApplicationContext;

/**
 * Implementation of {@link Variable} representing a single field in a table (just for testing)
 *
 * Created by rmemoria on 10/9/16.
 */
public class SimpleFieldVariable implements Variable {

    private static final VariableOptions OPTIONS = new VariableOptions(false, false, 0, new Item<>("cases", "Cases"));
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
    public String createKey(Object values) {
        return values != null ? values.toString() : null;
    }

    @Override
    public String getKeyDisplay(String key) {
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
    public VariableOptions getVariableOptions() {
        return OPTIONS;
    }

    @Override
    public String createGroupKey(Object values) {
        return values != null ? values.toString() : null;
    }

    @Override
    public String getGroupKeyDisplay(String key) {
        return key != null ? key.toString() : "-";
    }

    @Override
    public void initialize(ApplicationContext context) {

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
