package org.msh.etbm.commons.sqlquery;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Help building queries, decentralizing the query definitions
 *
 * Created by rmemoria on 15/8/16.
 */
public class SQLQueryBuilder implements QueryDefs {


    public static final Pattern TABLEALIAS_PATTERN = Pattern.compile("(\\$?\\w*\\.)");

    public static final String ROOT_TABLE_ALIAS = "a";

    /**
     * The main table of the SQL statement
     */
    private String tableName;

    /**
     * The list of parameters
     */
    private Map<String, Object> parameters = new HashMap<>();

    /**
     * The list of fields to be returned by the query
     */
    private List<SQLField> fields = new ArrayList<>();

    /**
     * The list of table joins
     */
    private List<SQLTable> joins = new ArrayList<>();

    /**
     * The list of restrictions to be included in the WHERE clause
     */
    private List<String> restrictions = new ArrayList<>();

    /**
     * The order by expression of the SQL statement
     */
    private String orderBy;

    /**
     * Used internally during the construction of the query definition
     */
    private QueryDefsImpl queryDefs;

    /**
     * Set the initial record of the result set
     */
    private Integer firstResult;

    /**
     * Set the max number of records to be returned
     */
    private Integer maxResult;

    /**
     * Generated when SQL is created. Maps all logical field names to the SQL field object
     */
    private Map<String, SQLField> fieldMapping;

    /**
     * List of named joins that are not initially included in the SQL. These joins are just used
     * inside queries if there is any reference on them
     */
    private Map<String, SQLTable> namedJoins = new HashMap<>();


    private List<QueryDefsListener> listeners;

    private int aliasCounter;

    /**
     * If true, fields in the SELECT clause will not be declared with alias
     */
    private boolean disableFieldAlias;



    /**
     * The default constructor
     * @param tableName The name of the main table used in the SQL FROM clause
     */
    public SQLQueryBuilder(String tableName) {
        this.tableName = tableName;

        // initialize query defs
        SQLTable table = SQLTable.createRoot(tableName);
        joins.add(table);
        table.setTableAlias(ROOT_TABLE_ALIAS);
        queryDefs = new QueryDefsImpl(this, table, null);
    }


    /**
     * Generate the SQL statement
     * @return String containing SQL statement
     */
    public String generate() {
        aliasCounter = 0;

        return generateSelect().toString() +
                generateFrom() +
                generateJoins() +
                generateWhere() +
                generateOrderBy() +
                generateGroupBy() +
                limitResultSet();
    }

    /**
     * Initialize the SQL generator for a new query, cleaning all restrictions, fields, joins
     */
    public void initialize() {
        parameters.clear();
        fields.clear();

        SQLTable tbl = joins.get(0);
        joins.clear();
        joins.add(tbl);

        restrictions.clear();
    }


    /**
     * Generate the SELECT clause of the SQL statement
     * @return instance of the StringBuilder class containing the select clause
     */
    private StringBuilder generateSelect() {
        StringBuilder s = new StringBuilder();

        if (fields.isEmpty()) {
            return s.append("select * ");
        }

        Map<String, SQLField> mapping = new HashMap<>();

        String delim = "select ";
        int index = 1;
        for (SQLField field: fields) {
            field.setIndex(index++);

            // add field to the SELECT clause
            s.append(delim);

            if (field.isAggregation()) {
                s.append(field.getFieldName());
            } else {
                String fname = field.getFieldName();
                String falias = disableFieldAlias ? "" : " " + field.getFieldAlias();

                if (fname.indexOf(".") > 0) {
                    fname = parseTableName(fname);
                    s.append(fname).append(falias);
                } else {
                    if (!isFieldExpression(fname)) {
                        s.append(field.getTable().getTableAlias()).append(".");
                    }

                    s.append(fname).append(falias);
                }
            }

            delim = ", ";

            // mount list of logical names
            String logicalName = field.getTable().getTableName() + '.' + field.getFieldName();
            mapping.put(logicalName, field);
        }

        fieldMapping = mapping;

        return s;
    }

    /**
     * Check if field is an expression (constant, subquery, etc) or is a literal
     * @param fname The field name
     * @return true if field is an expression
     */
    private boolean isFieldExpression(String fname) {
        char[] vals = {'"', '\'', ' ', '(' };

        for (char c: vals) {
            if (fname.indexOf(c) >= 0) {
                return true;
            }
        }

        return false;
    }


    /**
     * Generate the FROM clause of the SQL statement
     * @return instance of StringBuilder containing the FROM clause
     */
    protected StringBuilder generateFrom() {
        StringBuilder b = new StringBuilder();

        b.append("\nfrom ")
                .append(tableName)
                .append(' ')
                .append(ROOT_TABLE_ALIAS);

        return b;
    }

    /**
     * Generate the joins of the SQL statement
     * @return
     */
    protected StringBuilder generateJoins() {
        StringBuilder s = new StringBuilder();

        for (SQLTable join: joins) {
            if (join.isRoot()) {
                continue;
            }

            s.append('\n')
                    .append(join.isLeftJoin() ? "left join " : "join ")
                    .append(join.getTableName())
                    .append(' ')
                    .append(join.getTableAlias())
                    .append(" on ")
                    .append(join.getOn());
        }
        return s;
    }


    /**
     * Generate the WHERE clause of the SQL statement
     * @return instance of StringBuilder containing the WHERE clause
     */
    protected StringBuilder generateWhere() {
        StringBuilder s = new StringBuilder();

        if (restrictions.size() > 0) {

            String delim = "\nwhere ";
            for (String restriction: restrictions) {
                s.append(delim).append(restriction);
                delim = "\nand ";
            }
        }

        return s;
    }


    /**
     * Generate order by clause
     * @return
     */
    protected String generateOrderBy() {
        if (orderBy == null || orderBy.isEmpty()) {
            return "";
        }

        String parsedOrderBy = parseTableName(orderBy);

        return "\norder by " + parsedOrderBy;
    }


    protected String generateGroupBy() {
        List<SQLField> lst = fields.stream()
                .filter(field -> field.isAggregation())
                .collect(Collectors.toList());

        if (lst.size() == 0) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        String delim = "\ngroup by ";
        for (SQLField field: fields) {
            if (!field.isAggregation()) {
                String fname = disableFieldAlias ? field.getFieldName() : field.getFieldAlias();
                s.append(delim).append(fname);
            }

            delim = ", ";
        }

        return s.toString();
    }

    /**
     * Limit the number of records returned by the query. Support for paginagion
     * @return the SQL clause to be appended to the SQL
     */
    protected String limitResultSet() {
        StringBuilder s = new StringBuilder();

        if (maxResult != null) {
            s.append("\nlimit " + maxResult);
        }

        if (firstResult != null) {
            s.append("\noffset " + firstResult);
        }

        return s.toString();
    }


    /**
     * Parse the SQL expression and remove
     * @param sqlexpr
     * @return
     */
    protected String parseTableName(String sqlexpr) {
        Matcher matcher = TABLEALIAS_PATTERN.matcher(sqlexpr);
        while (matcher.find()) {
            String s = matcher.group();
            String tblName = s.substring(0, s.length() - 1);
            SQLTable tbl = tableByName(tblName);
            if (tbl == null) {
                throw new IllegalArgumentException("Table name not found: " + sqlexpr);
            }
            sqlexpr = sqlexpr.replace(s, tbl.getTableAlias() + '.');
        }

        return sqlexpr;
    }

    /**
     * Search for a table by its name
     * @param table the name of the table
     * @return instance of {@link SQLTable} or null if table is not found
     */
    public SQLTable tableByName(String table) {
        for (SQLTable tbl: joins) {
            if ((tbl.getTableName().equals(table)) || (tbl.getJoinName().equals(table))) {
                return tbl;
            }
        }
        return null;
    }


    /**
     * Search a field by its logical name. The logical name is in the format "tableName.fieldName".
     * Table name is the name of the main table or any table in the join.
     * @param fname the name of the table
     * @return
     */
    public SQLField fieldByName(String fname) {
        String logicalName = fname.indexOf('.') > 0 ? fname : tableName + '.' + fname;

        return fieldMapping.get(logicalName);
    }


    public SQLQueryBuilder restrict(String sqlexpr) {
        queryDefs.restrict(sqlexpr);
        return this;
    }

    public SQLQueryBuilder restrict(String sqlexpr, Object... parameters) {
        queryDefs.restrict(sqlexpr, parameters);
        return this;
    }

    @Override
    public QueryDefs join(String tableName, String on) {
        return queryDefs.join(tableName, on);
    }

    @Override
    public QueryDefs leftJoin(String tableName, String on) {
        queryDefs.leftJoin(tableName, on);
        return this;
    }

    @Override
    public QueryDefs join(String joinName) {
        return queryDefs.join(joinName);
    }

    public SQLQueryBuilder select(String fields) {
        // TODO: [MSANTOS] Essa linha foi necessária para a segunda query de units. O metodo from acrescentava unit.* no
        // inicio do select, fazendo com que a query retornasse todos os campos de units desnecessariamente.
        this.fields.clear();

        queryDefs.select(fields);
        return this;
    }

    @Override
    public String getMainTable() {
        return tableName;
    }

    /**
     * Add a named join. A named join can be used as reference
     * @param name
     * @param table
     * @param on
     */
    public void addNamedJoin(String name, String table, String on) {
        SQLTable tbl = new SQLTable();
        tbl.setTableName(table);
        tbl.setOn(on);
        namedJoins.put(name, tbl);
    }

    public void addNamedLeftJoin(String name, String table, String on) {
        SQLTable tbl = new SQLTable();
        tbl.setTableName(table);
        tbl.setOn(on);
        tbl.setLeftJoin(true);
        namedJoins.put(name, tbl);
    }

    /**
     * Serach for a named join by its name
     * @param joinName
     * @return
     */
    public SQLTable findNamedJoin(String joinName) {
        return namedJoins.get(joinName);
    }

    /**
     * Add an expression to the SQL SELECT clause that will be considered an expression, like a COUNT or SUM
     * @param expr
     */
    public void addGroupExpression(String expr) {
        queryDefs.createField(expr, true);
    }

    /**
     * Return the instance of the {@link QueryDefs} interface used internally
     * @return instance of {@link QueryDefs}
     */
    public QueryDefs getQueryDefs() {
        return queryDefs;
    }

    /**
     * Add a new listener to receive information about certain injections done by {@link QueryDefs} interface
     * @param listener the instance of {@link QueryDefsListener}
     */
    public void addListener(QueryDefsListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(listener);
    }

    /**
     * Remove a listener previously included with the {@link SQLQueryBuilder#addListener(QueryDefsListener)} method
     * @param listener
     */
    public void remListener(QueryDefsListener listener) {
        if (listeners == null) {
            return;
        }
        listeners.remove(listener);
    }

    /**
     * Remove all listeners previously included
     */
    public void clearListeners() {
        listeners = null;
    }

    /**
     * Return the SQL parameters to be used when executing the query
     * @return
     */
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getTableName() {
        return tableName;
    }

    public List<String> getRestrictions() {
        return Collections.unmodifiableList(restrictions);
    }

    public List<SQLField> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public List<SQLTable> getJoins() {
        return Collections.unmodifiableList(joins);
    }

    /**
     * Add a field to the list of fields to select
     * @param field
     */
    protected void addField(SQLField field) {
        fields.add(field);

        // check if there are listeners to that event
        if (listeners != null) {
            for (QueryDefsListener listener: listeners) {
                listener.onInjectedField(field);
            }
        }
    }

    protected void addJoin(SQLTable join) {
        joins.add(join);
    }

    protected void addRestriction(String restriction) {
        restrictions.add(restriction);
    }

    protected void addParameter(String param, Object value) {
        parameters.put(param, value);
    }

    public Integer getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(Integer firstResult) {
        this.firstResult = firstResult;
    }

    public Integer getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(Integer maxResult) {
        this.maxResult = maxResult;
    }


    /**
     * Create a new alias for a table join
     * @return
     */
    protected String createTableAlias() {
        int index = aliasCounter / 27;
        int letter = aliasCounter % 27;

        aliasCounter++;

        String alias = (char)(97 + letter) + Integer.toString(index);
        return alias;
    }

    /**
     * Return an instance of the {@link SQLQueryBuilder} using the given table
     * @param tableName
     * @return
     */
    public static SQLQueryBuilder from(String tableName) {
        return new SQLQueryBuilder(tableName);
    }

    public boolean isDisableFieldAlias() {
        return disableFieldAlias;
    }

    public void setDisableFieldAlias(boolean disableFieldAlias) {
        this.disableFieldAlias = disableFieldAlias;
    }
}
