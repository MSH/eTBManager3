package org.msh.etbm.test.commons.indicators.fixtures;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.indicators.keys.Key;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.indicators.variables.VariableOutput;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.services.cases.filters.CaseFilters;
import org.springframework.context.ApplicationContext;

/**
 * Implementation of {@link Variable} representing a single field in a table (just for testing)
 *
 * Created by rmemoria on 10/9/16.
 */
public class SimpleFieldVariable implements Variable {

    private String id;
    private String label;
    private String fieldName;

    private ApplicationContext context;


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
    public Key createKey(Object[] values, int iteration) {
        return values[0] != null ? Key.of(values[0]) : Key.asNull();
    }

    @Override
    public String getKeyDisplay(Key key) {
        return key.isNull() ? getMessages().get(Messages.UNDEFINED) : key.getValue().toString();
    }

    @Override
    public int compareValues(Key val1, Key val2) {
        return val1.compareTo(val2);
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
    public int getIterationCount() {
        return 1;
    }

    @Override
    public boolean isTotalEnabled() {
        return true;
    }

    @Override
    public VariableOutput getVariableOutput() {
        return CaseFilters.VAROUT_CASES;
    }


    @Override
    public String getGroupKeyDisplay(Key key) {
        return key != null ? key.toString() : "-";
    }

    @Override
    public void initialize(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return label;
    }

    private Messages getMessages() {
        return context != null ? context.getBean(Messages.class) : null;
    }
}
