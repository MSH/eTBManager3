package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.sqlquery.SQLField;

/**
 * Store temporary information about the field assigned to a variable during the construction of the
 * SQL query by the {@link IndicatorSqlBuilder} class
 *
 * Created by rmemoria on 10/9/16.
 */
public class VariableField {
    /**
     * The field in the SQL query
     */
    private SQLField sqlField;

    /**
     * The variable assigned to the field
     */
    private Variable variable;

    public VariableField(SQLField sqlField, Variable variable) {
        this.sqlField = sqlField;
        this.variable = variable;
    }

    public SQLField getSqlField() {
        return sqlField;
    }

    public Variable getVariable() {
        return variable;
    }
}
