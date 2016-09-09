package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.FilterValue;
import org.msh.etbm.commons.indicators.filters.Filter;
import org.msh.etbm.commons.indicators.filters.ValueHandler;
import org.msh.etbm.commons.indicators.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create a SQL declaration to be used in indicator org.msh.etbm.commons.indicators
 * @author Ricardo Memoria
 *
 */
public class SqlBuilder implements SQLDefs {

    private TableJoinImpl masterTable;
    private StringBuilder sql;
    private List<Field> fields = new ArrayList<Field>();
    private List<Variable> variables = new ArrayList<Variable>();
    private Variable currentVariable;
    private String fieldList;
    private List<String> restrictions = new ArrayList<String>();
    private List<String> varRestrictions = new ArrayList<String>();
    private HashMap<String, Object> parameters = new HashMap<String, Object>();
    private Map<Variable, Integer> varIteration = new HashMap<Variable, Integer>();
    // joins declared by the variable during SQL creation
    private List<TableJoinImpl> varJoins = new ArrayList<TableJoinImpl>();
    private boolean detailed;
    private String orderBy;
    private Map<Filter, FilterValue> filters;
    // true if in the loop to create the SQL
    private boolean creatingSql;

    /**
     * Constructor using a root table name as argument
     * @param tableName the main table of the query
     */
    public SqlBuilder(String tableName) {
        super();
        masterTable = new TableJoinImpl(this, tableName, null, null, null);
    }

    /**
     * Default constructor
     */
    public SqlBuilder() {
        super();
    }


    /**
     * Clear the data to generate a new SQL from scratch
     */
    public void clear() {
        sql = null;
        fields.clear();
        variables.clear();
        currentVariable = null;
        restrictions.clear();
        fieldList = null;
        varIteration.clear();
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
     * Change the table name and clear all joins
     * @param tableName
     */
    public void setTableName(String tableName) {
        masterTable = new TableJoinImpl(this, tableName, null, null, null);
    }


    /**
     * Return the main table name used in this query
     * @return String value
     */
    public String getTableName() {
        return masterTable != null ? masterTable.getTableName() : null;
    }


    /**
     * Create the SQL instruction
     * @return SQL instruction to be executed in the database server in order to return
     * the specific fields and its indicators
     */
    public String createSql() {
        sql = new StringBuilder();
        fieldList = null;
        varRestrictions.clear();
        varJoins.clear();

        creatingSql = true;
        try {
            // include filters in the SQL
            if (filters != null) {
                for (Filter filter: filters.keySet()) {
                    FilterValue fvalue = filters.get(filter);
                    ValueHandler fv = new ValueHandler(fvalue.getValue().toString(), filter.isMultiSelection());
                    filter.prepareFilterQuery(this, fvalue.getComparator(), fv);
                }
            }

            if (variables.size() > 0) {
                fields.clear();
                try {
                    for (Variable var: variables) {
                        currentVariable = var;
                        // get the current variable iteration
                        Integer iteration = varIteration.get(var);
                        if (iteration == null) {
                            iteration = 0;
                        }

                        // prepare the variable
                        var.prepareVariableQuery(this, iteration);
                    }
                } finally {
                    currentVariable = null;
                }
            }

            createSQLSelect(sql);

            createSQLFrom(sql);

            createSQLJoins(sql);

            createSQLWhere(sql);

            createSQLGroupBy(sql);

            createSQLOrderBy(sql);

            // clear joins added by variables during construction
            for (TableJoinImpl join: varJoins) {
                if (join.getParentJoin() != null) {
                    join.getParentJoin().removeJoin(join);
                }
            }

            // remove all joins that were declared during the creation of the SQL
            masterTable.removeIterationContextJoins();
        } finally {
            creatingSql = false;
        }

        return sql.toString();
    }


    /**
     * Create SQL Where clause
     */
    protected void createSQLWhere(StringBuilder builder) {
        boolean first = true;

        // include restrictions defined in the SQL builder
        if (restrictions.size() > 0) {
            for (String s: restrictions) {
                if (first) {
                    builder.append("\nwhere ");
                    first = false;
                } else {
                    builder.append("\nand ");
                }

                s = parseTableNames(s);
                builder.append("\n" + s);
            }
        }

        // include restrictions defined by the variables
        if (varRestrictions.size() > 0) {
            for (String s: varRestrictions) {
                if (first) {
                    builder.append("\nwhere ");
                    first = false;
                } else {
                    builder.append("\nand ");
                }

                s = parseTableNames(s);
                builder.append("\n" + s);
            }
        }
    }


    /**
     * Return the list of column for a given variable used to build the query
     * @param var instance of {@link Variable} used to create the query
     * @return zero-based index of the fields returned by the query and related to the variable
     */
    public int[] getColumnsVariable(Variable var) {
        List<Integer> cols = new ArrayList<>();
        int index = 0;
        for (Field field: fields) {
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
     * Create SELECT SQL instruction
     */
    protected void createSQLSelect(StringBuilder builder) {
        builder.append("select ");

        if (detailed) {
            builder.append( getFieldList() );
        } else {
            if (variables.size() == 0) {
                builder.append("count(*)");
            } else {
                builder.append(getFieldList());
                builder.append(", count(*)");
            }
        }
    }


    /**
     * Create SQL FROM
     */
    protected void createSQLFrom(StringBuilder builder) {
        builder.append("\nfrom " + masterTable.getTableName() + " " + masterTable.getAlias());
    }

    /**
     * Create SQL join with other tables
     */
    protected void createSQLJoins(StringBuilder builder) {
        if (masterTable.getJoins() != null) {
            for (TableJoinImpl join : masterTable.getJoins()) {
                createSQLJoin(builder, join);
            }
        }
    }

    /**
     * Create a join of the table and its joins
     * @param join
     */
    protected void createSQLJoin(StringBuilder builder, TableJoinImpl join) {
        if (join.isLeftJoin()) {
            builder.append("\nleft join ");
        } else {
            builder.append("\ninner join ");
        }

        builder.append(join.getTableName());
        builder.append(' ');
        builder.append(join.getAlias());
        builder.append(" on ");
        TableJoin p = join.getParentJoin();
        builder.append(join.getAlias() + "." + join.getTableField() + "=" + p.getAlias() + "." + join.getParentField());

        if (join.getJoins() != null) {
            for (TableJoinImpl j : join.getJoins()) {
                createSQLJoin(builder, j);
            }
        }
    }


    /**
     * Create SQL Group By instruction
     */
    protected void createSQLGroupBy(StringBuilder builder) {
        if ((detailed) || (variables.size() == 0)) {
            return;
        }
        builder.append("\ngroup by ");
        builder.append( parseTableNames( getFieldList() ));
    }


    /**
     * Create order by clause. If the query is detailed, so the order by
     * will just be included if the <code>orderBy</code> is defined.
     * @param builder
     */
    protected void createSQLOrderBy(StringBuilder builder) {
        if ((detailed && orderBy == null) || ((!detailed) && (variables.size() == 0))) {
            return;
        }

        builder.append("\norder by ");

        if (orderBy != null) {
            builder.append(parseTableNames(orderBy));
        } else {
            builder.append( parseTableNames(getFieldList()));
        }
    }


    /**
     * Return the field list separated by commas
     * @return
     */
    protected String getFieldList() {
        if (fieldList == null) {
            fieldList = "";
            for (Field field: fields) {
                if (!fieldList.isEmpty()) {
                    fieldList += ", ";
                }

                if (field.getTable() == null) {
                    fieldList += parseTableNames(field.getName());
                } else {
                    fieldList += field.getTable().getAlias() + "." + field.getName();
                }
            }
        }

        return fieldList;
    }

    /**
     * Replace table names by its alias. In the given string, search for declarations of
     * the table in the format <code>table.field</code> and replaces by its table alias
     * @param s declaration containing the table declarations
     * @return parsed String with table names replaced by its alias
     */
    protected String parseTableNames(String s) {
        int index = 0;
        while (index < s.length()) {
            index = s.indexOf('.', index);
            if (index == -1) {
                break;
            }

            int ini = index - 1;
            while (ini >= 0) {
                char c = s.charAt(ini);
                if (!Character.isJavaIdentifierPart(c)) {
                    break;
                }
                ini--;
            }
            if (ini < 0) {
                ini = 0;
            } else {
                ini++;
            }

            String tbl = s.substring(ini, index);
            TableJoin join = masterTable.findJoin(tbl);

            if (join != null) {
                String alias = join.getAlias();
                s = s.substring(0, ini) + alias + s.substring(index, s.length());
                index = ini + alias.length() + 1;
            } else {
                index++;
            }
        }

        return s;
    }

    /**
     * Add a variable to the SQL Builder
     * @param var instance of {@link Variable} to have its values returned by the query
     */
    public void addVariable(Variable var) {
        variables.add(var);
    }

    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.query.SQLDefs#getMasterTable()
     */
    @Override
    public TableJoin getMasterTable() {
        return masterTable;
    }


    /** {@inheritDoc}
     */
    public void select(String field, TableJoin table) {
        Field fld = new Field();
        fld.setName(field);
        fld.setTable(table);
        fld.setVariable(currentVariable);
        fields.add(fld);
    }

    /** {@inheritDoc}
     */
    @Override
    public void select(String field) {
        select(field, null);
    }

    /**
     * Find a table join by its table name or table alias
     * @param table is the table name or table alias
     * @return instance of the {@link TableJoin} class
     */
    protected TableJoinImpl findTable(String table) {
        if ((table.equals(masterTable.getTableName())) || (table.equals(masterTable.getAlias()))) {
            return masterTable;
        }

        for (TableJoinImpl join: masterTable.getJoins()) {
            if ((table.equals(join.getTableName())) || (table.equals(join.getAlias()))) {
                return join;
            }
        }

        return null;
    }


    /**
     * Store temporary information about a field of the query
     * @author Ricardo Memoria
     *
     */
    public class Field {
        private String name;
        private TableJoin table;
        private Variable variable;

        /**
         * @return the name
         */

        public String getName() {
            return name;
        }
        /**
         * @param name the name to set
         */

        public void setName(String name) {
            this.name = name;
        }
        /**
         * @return the table
         */

        public TableJoin getTable() {
            return table;
        }
        /**
         * @param table the table to set
         */

        public void setTable(TableJoin table) {
            this.table = table;
        }
        /**
         * @return the variable
         */

        public Variable getVariable() {
            return variable;
        }

        /**
         * @param variable the variable to set
         */
        public void setVariable(Variable variable) {
            this.variable = variable;
        }
    }


    /** {@inheritDoc}
     */
    @Override
    public void addRestriction(String restriction) {
        List<String> restr = currentVariable != null ? varRestrictions : restrictions;

        if (!restr.contains(restriction)) {
            restr.add(restriction);
        }
    }


    /**
     * Return the list of parameters and its values used in the query
     * @return the parameters
     */
    public HashMap<String, Object> getParameters() {
        return parameters;
    }


    /* (non-Javadoc)
     * @see org.msh.etbm.commons.indicators.query.SQLDefs#addParameter(java.lang.String, java.lang.Object)
     */
    @Override
    public void addParameter(String paramname, Object value) {
        parameters.put(paramname, value);
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
        return detailed;
    }


    /**
     * Set the builder to generate a detailed or consolidated query
     * @param detailed boolean value
     */
    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }


    /**
     * Return the SQL order by declaration to be used when generating the SQL
     * @return the orderBy
     */
    public String getOrderBy() {
        return orderBy;
    }


    /**
     * Set the SQL order by declaration to be used when generating the SQL
     * @param orderBy the orderBy to set
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }


    /** {@inheritDoc}
     */
    @Override
    public FilterValue getFilterValue(String filterid) {
        // filters were set ?
        if (filters == null) {
            return null;
        }

        // search for the filter by its id
        for (Filter filter: filters.keySet()) {
            if (filterid.equals(filter.getId())) {
                return filters.get(filter);
            }
        }
        // no filter found, return null
        return null;
    }


    /**
     * @return the filters
     */
    public Map<Filter, FilterValue> getFilters() {
        return filters;
    }


    /**
     * @param filters the filters to set
     */
    public void setFilters(Map<Filter, FilterValue> filters) {
        this.filters = filters;
    }


    /** {@inheritDoc}
     */
    @Override
    public TableJoin table(String table) {
        return findTable(table);
    }


    /** {@inheritDoc}
     */
    @Override
    public TableJoin join(String newTable, String parentTable) {
        String[] s = parentTable.split("\\.");
        TableJoinImpl tbl = findTable(s[0]);
        if (tbl == null) {
            throw new RuntimeException("Parent table of the join was not found: " + parentTable);
        }

        return tbl.join(s[1], newTable);
    }

    /** {@inheritDoc}
     */
    @Override
    public TableJoin join(String newTable, String newField, String parentTable, String parentField) {
        TableJoinImpl tbl = findTable(parentTable);

        return tbl.join(parentField, newTable, newField);
    }


    /**
     * Return true if the object is creating the SQL declaration
     * @return boolean value
     */
    public boolean isCreatingSql() {
        return creatingSql;
    }
}
