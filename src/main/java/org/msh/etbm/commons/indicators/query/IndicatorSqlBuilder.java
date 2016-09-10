package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.sqlquery.QueryDefs;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create a SQL declaration to be used in indicator org.msh.etbm.commons.indicators
 * @author Ricardo Memoria
 *
 */
public class IndicatorSqlBuilder {

    private SQLQueryBuilder sqlBuilder;

    private List<Variable> variables = new ArrayList<Variable>();

    // list of fields and its assigned variables included during query building
    private List<VariableField> variableFields = new ArrayList<>();

    // the list of fields to return by the query. If this property is set, the query will not be used
    // for indicator generator, but for query details
    private String detailedField;

    private Variable currentVariable;
    private String fieldList;
    private List<String> varRestrictions = new ArrayList<String>();
    private Map<Variable, Integer> varIteration = new HashMap<Variable, Integer>();
    private Map<Filter, Object> filters;


    /**
     * Constructor using a root table name as argument
     * @param tableName the main table of the query
     */
    public IndicatorSqlBuilder(String tableName) {
        sqlBuilder = new SQLQueryBuilder(tableName);
        sqlBuilder.addListener(field -> {
            variableFields.add(new VariableField(field, currentVariable));
        });
    }


    /**
     * Return the interface {@link QueryDefs} to externalize injection of
     * query restrictions, fields, joins, etc.
     *
     * @return instance of {@link QueryDefs}
     */
    public QueryDefs getQueryDefs() {
        return sqlBuilder;
    }

    /**
     * Select the iteration of the variable to be executed
     * @param var the variable that is already available in the list of variables
     * @param iteration the iteration index to be executed, starting at 0 index
     */
    public void setVariableIteration(Variable var, Integer iteration) {
        varIteration.put(var, iteration);
    }


    /**
     * Return the main table name used in this query
     * @return String value
     */
    public String getTableName() {
        return sqlBuilder.getTableName();
    }


    /**
     * Create the SQL instruction
     * @return SQL instruction to be executed in the database server in order to return
     * the specific fields and its indicators
     */
    public String createSql() {
        fieldList = null;
        varRestrictions.clear();
        sqlBuilder.clearSelect();

        // include filters in the SQL
        if (filters != null) {
            for (Filter filter: filters.keySet()) {
                Object fvalue = filters.get(filter);
                filter.prepareFilterQuery(getQueryDefs(), fvalue, null);
            }
        }

        if (variables.size() > 0) {
            try {
                for (Variable var: variables) {
                    currentVariable = var;
                    // get the current variable iteration
                    Integer iteration = varIteration.get(var);
                    if (iteration == null) {
                        iteration = 0;
                    }

                    // prepare the variable
                    var.prepareVariableQuery(getQueryDefs(), iteration);
                }
            } finally {
                currentVariable = null;
            }
        }

        return sqlBuilder.generate();
    }



    /**
     * Return the list of column for a given variable used to build the query
     * @param var instance of {@link Variable} used to create the query
     * @return zero-based index of the fields returned by the query and related to the variable
     */
    public int[] getColumnsVariable(Variable var) {
        List<Integer> cols = new ArrayList<>();
        int index = 0;
        for (VariableField field: variableFields) {
            if (field.getVariable() == var) {
                cols.add(index);
            }
            index++;
        }

        int[] res = new int[cols.size()];
        index = 0;
        for (Integer n: cols) {
            res[index++] = n;
        }

        return res;
    }


    /**
     * Add a variable to the SQL Builder
     * @param var instance of {@link Variable} to have its values returned by the query
     */
    public void addVariable(Variable var) {
        variables.add(var);
    }


    /**
     * Return the list of parameters and its values used in the query
     * @return the parameters
     */
    public Map<String, Object> getParameters() {
        return sqlBuilder.getParameters();
    }


    /**
     * Return list of variables used to build the query
     * @return the variables
     */
    public List<Variable> getVariables() {
        return variables;
    }


    /**
     * Return true if the builder will generate a detailed query (where other fields may be included)
     * or a consolidated query (where just the variable fields will be used with a count(*) declaration)
     * @return boolean value
     */
    public boolean isDetailed() {
        return detailedField != null;
    }

    public void setDetailedField(String detailedField) {
        this.detailedField = detailedField;
    }

    public String getDetailedField() {
        return detailedField;
    }

    /**
     * Return the SQL order by declaration to be used when generating the SQL
     * @return the orderBy
     */
    public String getOrderBy() {
        return sqlBuilder.getOrderBy();
    }


    /**
     * Set the SQL order by declaration to be used when generating the SQL
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.sqlBuilder.setOrderBy(orderBy);
    }



    /**
     * @return the filters
     */
    public Map<Filter, Object> getFilters() {
        return filters;
    }


    /**
     * @param filters the filters to set
     */
    public void setFilters(Map<Filter, Object> filters) {
        this.filters = filters;
    }
}
