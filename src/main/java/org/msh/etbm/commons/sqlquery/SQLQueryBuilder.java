package org.msh.etbm.commons.sqlquery;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Help building queries, decentralizing the query definitions
 *
 * Created by rmemoria on 15/8/16.
 */
public class SQLQueryBuilder implements QueryDefs {


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
    private QueryDefs queryDefs;




    /**
     * The default constructor
     * @param tableName
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
        if (fields.size() == 0) {
            return null;
        }

        StringBuilder s = new StringBuilder();

        s.append(generateSelect())
                .append(generateFrom())
                .append(generateJoins())
                .append(generateWhere())
                .append(generateOrderBy())
                .append(generateGroupBy());

        return s.toString();
    }

    /**
     * Generate the SELECT clause of the SQL statement
     * @return
     */
    protected StringBuilder generateSelect() {
        StringBuilder s = new StringBuilder();

        String delim = "select ";
        for (SQLField field: fields) {
            s.append(delim)
                    .append(field.getTable().getTableAlias())
                    .append(".")
                    .append(field.getFieldName()).append(' ').append(field.getFieldAlias());
            delim = ", ";
        }

        return s;
    }


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
                    .append(join.isLeftJoin() ? "left join ": "join ")
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
     * @return
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

        return "\norder by " + orderBy;
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
        for (SQLField field: lst) {

        }

        return s.toString();
    }

    @Override
    public QueryDefs restrict(String sqlexpr) {
        return queryDefs.restrict(sqlexpr);
    }

    @Override
    public QueryDefs restrict(String sqlexpr, Object... parameters) {
        return queryDefs.restrict(sqlexpr, parameters);
    }

    @Override
    public QueryDefs join(String tableName, String on) {
        return queryDefs.join(tableName, on);
    }

    @Override
    public QueryDefs leftJoin(String tableName, String on) {
        return queryDefs.leftJoin(tableName, on);
    }

    @Override
    public QueryDefs select(String fields) {
        return queryDefs.select(fields);
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

    protected void addField(SQLField field) {
        fields.add(field);
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
}
