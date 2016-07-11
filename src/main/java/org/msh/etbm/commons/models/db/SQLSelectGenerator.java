package org.msh.etbm.commons.models.db;

import ch.qos.logback.classic.db.SQLBuilder;
import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.models.data.fields.Field;
import org.msh.etbm.commons.models.data.handlers.FieldHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate SQL statement to retrieve a record model from the database
 *
 * Created by rmemoria on 8/7/16.
 */
public class SQLSelectGenerator {

    public static final String ROOT_TABLE_ALIAS = "a";
    public static final Pattern TABLEALIAS_PATTERN = Pattern.compile("(\\w*\\.)");

    /**
     * Generate SQL select to recover the record by its ID
     * @param model The model to generate the query from
     * @param id the ID of the record to recover
     * @param displaying true if fields must include information to be displayed
     * @return The SQL to select values and its query parameters
     */
    public SQLSelectResult generateByID(Model model, Object id, boolean displaying) {
        StringBuilder s = new StringBuilder();

        SQLSelectResult res = collectDBFieldsFromModel(model, displaying);

        if (res.getFields() == null) {
            return null;
        }

        generateSQLSelect(s, res);

        // include FROM clause
        s.append("\nfrom " + model.getTable()).append(' ').append(ROOT_TABLE_ALIAS);

        // include table joins
        if (res.getTables() != null) {
            for (SQLJoinedTable tbl: res.getTables()) {
                s.append('\n')
                        .append("join ")
                        .append(tbl.getTableName())
                        .append(" on ")
                        .append(parseTableNames(res, tbl.getOn()));
            }
        }

        res.setSql(s.toString());

        return res;
    }

    /**
     * Generate SQL Select statement, with all fields part of the select operation
     * @param s the StringBuilder containing the SQL struction
     * @param res partial information about the SQL to build
     */
    private void generateSQLSelect(StringBuilder s, SQLSelectResult res) {
        // include fields to select
        s.append("select ");

        String delim = "";
        for (SQLField f: res.getFields()) {
            String alias = f.getTable() != null ? f.getTable().getTableAlias() : ROOT_TABLE_ALIAS;
            s.append(delim)
                    .append(alias)
                    .append('.')
                    .append(f.getFieldName());
            delim = ", ";
        }
    }

    private SQLSelectResult collectDBFieldsFromModel(Model model, boolean displaying) {
        SQLSelectResult res = new SQLSelectResult(model);

        for (Field field: model.getFields()) {
            collectDBFieldsFromModelField(res, field, displaying);
        }

        return res;
    }

    private void collectDBFieldsFromModelField(SQLSelectResult res, Field field, boolean displaying) {
        FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());

        DBFieldsDefImpl fieldsDef = new DBFieldsDefImpl(field, res, res.getModel().resolveTableName());
        handler.dbFieldsToSelect(field, fieldsDef, displaying);
    }

    /**
     * Parse the sql snippet and replace table references by table alias
     * @param res The select result with information about tables and fields
     * @param sqlexpr the expression to be replaced
     * @return
     */
    protected String parseTableNames(SQLSelectResult res, String sqlexpr) {
        Matcher matcher = TABLEALIAS_PATTERN.matcher(sqlexpr);
        while (matcher.find()) {
            String s = matcher.group();
            String tbl = s.substring(0, s.length() - 1);
            sqlexpr = sqlexpr.replace(s, getTableAlias(res, tbl) + '.');
        }
        return sqlexpr;
    }

    /**
     * Return the table alias by the table name
     * @param res
     * @param tableName
     * @return
     */
    protected String getTableAlias(SQLSelectResult res, String tableName) {
        String rootTable = res.getModel().resolveTableName();
        if (tableName.equals(rootTable)) {
            return ROOT_TABLE_ALIAS;
        }

        for (SQLJoinedTable item: res.getTables()) {
            if (item.getTableName().equals(tableName)) {
                return item.getTableAlias();
            }
        }
        return tableName;
    }

}
