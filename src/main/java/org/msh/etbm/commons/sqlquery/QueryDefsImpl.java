package org.msh.etbm.commons.sqlquery;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rmemoria on 16/8/16.
 */
public class QueryDefsImpl implements QueryDefs {

    public static final Pattern TABLEALIAS_PATTERN = Pattern.compile("(\\$?\\w*\\.)");

    private SQLQueryBuilder builder;
    private SQLTable tableJoin;
    private SQLTable parent;

    public QueryDefsImpl(SQLQueryBuilder builder, SQLTable tableJoin, SQLTable parent) {
        this.builder = builder;
        this.tableJoin = tableJoin;
        this.parent = parent;
    }

    @Override
    public QueryDefs restrict(String sqlexpr) {

        String s = parseTableName(sqlexpr);

        builder.addRestriction(s);
        return this;
    }

    @Override
    public QueryDefs restrict(String sqlexpr, Object... paramValues) {
        int pos;
        int index = 0;

        // replace parameters
        String s = sqlexpr;
        while ((pos = s.indexOf("?")) >= 0) {
            String pname = generateParamName();
            s = s.substring(0, pos) + ':' + pname + s.substring(pos + 1);
            builder.addParameter(pname, paramValues[index]);
            index++;
        }

        s = parseTableName(s);

        builder.addRestriction(s);
        return this;
    }

    @Override
    public QueryDefs join(String tableName, String on) {
        QueryDefsImpl qd = addJoin(tableName, on);
        SQLTable tblJoin = qd.getTableJoin();

        return qd;
    }

    @Override
    public QueryDefs leftJoin(String tableName, String on) {
        QueryDefsImpl qd = addJoin(tableName, on);
        SQLTable tblJoin = qd.getTableJoin();
        tblJoin.setLeftJoin(true);

        return qd;
    }

    @Override
    public QueryDefs select(String fields) {
        String[] lst = fields.split(",");
        for (String f: lst) {
            String alias = createFieldAlias();
            SQLField field = new SQLField(f.trim(), alias, false, tableJoin);
            builder.addField(field);
        }
        return this;
    }


    protected QueryDefsImpl addJoin(String tableName, String on) {
        SQLTable tblJoin = new SQLTable();
        tblJoin.setTableName(tableName);
        String alias = createTableAlias();
        tblJoin.setTableAlias(alias);

        builder.addJoin(tblJoin);

        QueryDefsImpl qd = new QueryDefsImpl(builder, tblJoin, tableJoin);
        String newOn = qd.parseTableName(on);
        tblJoin.setOn(newOn);

        return qd;
    }

    /**
     * Generate a new parameter name
     * @return
     */
    private String generateParamName() {
        int num = builder.getParameters().size();
        return "p" + num;
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
            String tbl = s.substring(0, s.length() - 1);
            sqlexpr = sqlexpr.replace(s, getTableAlias(tbl) + '.');
        }

        return sqlexpr;
    }


    /**
     * Return the table alias by the table name
     * @param tableName
     * @return
     */
    protected String getTableAlias(String tableName) {
        if ("$this".equals(tableName) || tableJoin.getTableName().equals(tableName)) {
            return tableJoin.getTableAlias();
        }

        if ("$root".equals(tableName)) {
            return builder.ROOT_TABLE_ALIAS;
        }

        if ("$parent".equals(tableName)) {
            return parent != null ? parent.getTableAlias() : builder.ROOT_TABLE_ALIAS;
        }

        List<SQLTable> tables = builder.getJoins();
        for (SQLTable tbl: tables) {
            if (tbl.getTableName().equals(tableName)) {
                return tbl.getTableAlias();
            }
        }
        return tableName;
    }

    /**
     * Create a new alias for a table join
     * @return
     */
    protected String createTableAlias() {
        int count = builder.getJoins().size() + 1;

        int index = count / 27;
        int letter = count % 27;

        String alias = (char)(97 + letter) + Integer.toString(index);
        return alias;
    }

    /**
     * Create a new alias to be used in field declaration
     * @return
     */
    protected String createFieldAlias() {
        return 'f' + Integer.toString(builder.getFields().size());
    }

    public SQLTable getTableJoin() {
        return tableJoin;
    }

}
