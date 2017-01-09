package org.msh.etbm.commons.models.db;

import org.msh.etbm.commons.models.FieldTypeManager;
import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.Field;
import org.msh.etbm.commons.models.data.FieldHandler;
import org.msh.etbm.commons.models.data.Model;
import org.msh.etbm.commons.objutils.ObjectUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Generate SQL statement to retrieve a record model from the database
 *
 * Created by rmemoria on 8/7/16.
 */
public class SQLQuerySelectionBuilder {

    public static final String ROOT_TABLE_ALIAS = "a";
    public static final Pattern TABLEALIAS_PATTERN = Pattern.compile("(\\w*\\.)");

    private boolean displaying;
    private String customTableSuffix;

    /**
     * Generate SQL select to recover the record by its ID
     * @param model The model to generate the query from
     * @param restrictions the restrictions to be applied (inside where clause)
     * @return The SQL to select values and its query parameters
     */
    public SQLQueryInfo generate(Model model, String restrictions, UUID workspaceId) {
        StringBuilder s = new StringBuilder();

        SQLQueryInfo res = collectDBFieldsFromModel(model, displaying);

        if (res.getFields() == null) {
            return null;
        }

        generateSQLSelect(s, res);

        // include FROM clause
        s.append("\nfrom " + model.getTable())
                .append(' ')
                .append(ROOT_TABLE_ALIAS);

        // include table joins
        if (res.getTables() != null) {
            for (SQLJoinedTable tbl: res.getTables()) {
                String join = tbl.isLeftJoin() ? "left join " : "join ";
                s.append('\n')
                        .append(join)
                        .append(tbl.getTableName())
                        .append(' ').append(tbl.getTableAlias())
                        .append(" on ")
                        .append(parseTableNames(res, tbl.getOn()));
            }
        }

        s.append("\nwhere ").append(ROOT_TABLE_ALIAS).append(".workspace_id = :workspace_id");

        if (restrictions != null) {
            s.append("\nand ")
                    .append(parseTableNames(res, restrictions));
        }

        res.setSql(s.toString());
        res.setParameters(new HashMap<>());
        res.getParameters().put("workspace_id", ObjectUtils.uuidAsBytes(workspaceId));

        return res;
    }


    /**
     * Generate SQL Select statement, with all fields part of the select operation
     * @param s the StringBuilder containing the SQL struction
     * @param res partial information about the SQL to build
     */
    private void generateSQLSelect(StringBuilder s, SQLQueryInfo res) {
        // generate field alias names
        int index = 0;
        for (Map.Entry<Field, List<SQLQueryField>> entry: res.getFields().entrySet()) {
            Field field = entry.getKey();
            for (SQLQueryField fld: entry.getValue()) {
                fld.setFieldNameAlias("f" + index);
                index++;
            }
        }

        // include fields to select
        s.append("select ");

        // include ID to load
        s.append(ROOT_TABLE_ALIAS)
                .append('.')
                .append("id, ");

        String delim = "";
        for (Map.Entry<Field, List<SQLQueryField>> entry: res.getFields().entrySet()) {
            for (SQLQueryField f: entry.getValue()) {
                System.out.println(f.getFieldName());
                if (f.getFieldName().contains("$id")) {
                    System.out.println("oi");
                }
                String alias = f.getTable() != null ? f.getTable().getTableAlias() : ROOT_TABLE_ALIAS;
                s.append(delim)
                        .append(alias)
                        .append('.')
                        .append(f.getFieldName())
                        .append(" as ")
                        .append(f.getFieldNameAlias());
                delim = ", ";
            }
        }
    }

    private SQLQueryInfo collectDBFieldsFromModel(Model model, boolean displaying) {
        SQLQueryInfo res = new SQLQueryInfo(model);

        boolean customTblAdded = false;

        for (Field field: model.getFields()) {
            collectDBFieldsFromModelField(res, field, displaying);
        }

        return res;
    }


    /**
     * Return the instance of {@link SQLJoinedTable} representing the custom table used in the model
     * with custom fields
     * @param res Object with information about the SQL select
     * @return instance of {@link SQLJoinedTable}
     */
    private SQLJoinedTable getCustomTable(SQLQueryInfo res) {
        if (res.getCustomTable() != null) {
            return res.getCustomTable();
        }

        if (customTableSuffix == null) {
            throw new ModelException("No table suffix provided for custom fields");
        }

        // define a table to be declared in a left join in the SQL
        String rootTbl = res.getModel().resolveTableName();
        String customTbl = rootTbl + customTableSuffix;
        SQLJoinedTable tbl = new SQLJoinedTable(customTbl, customTbl + ".id = " + rootTbl + ".id", true);
        tbl.setTableAlias("ac");
        if (res.getTables() == null) {
            res.setTables(new ArrayList<>());
        }
        res.getTables().add(tbl);
        res.setCustomTable(tbl);

        return tbl;
    }

    private void collectDBFieldsFromModelField(SQLQueryInfo res, Field field, boolean displaying) {
        FieldHandler handler = FieldTypeManager.instance().getHandler(field.getTypeName());

        DBFieldsDefImpl rootDef = new DBFieldsDefImpl(field, res, res.getModel().resolveTableName());

        DBFieldsDefImpl fieldsDef = field.isCustom() ?
                new DBFieldsDefImpl(rootDef, getCustomTable(res), field, res, res.getModel().resolveTableName()) :
                rootDef;

        handler.dbFieldsToSelect(field, fieldsDef, displaying);
    }

    /**
     * Parse the sql snippet and replace table references by table alias
     * @param res The select result with information about tables and fields
     * @param sqlexpr the expression to be replaced
     * @return
     */
    protected String parseTableNames(SQLQueryInfo res, String sqlexpr) {
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
    protected String getTableAlias(SQLQueryInfo res, String tableName) {
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

    public boolean isDisplaying() {
        return displaying;
    }

    public void setDisplaying(boolean displaying) {
        this.displaying = displaying;
    }

    public String getCustomTableSuffix() {
        return customTableSuffix;
    }

    public void setCustomTableSuffix(String customTableSuffix) {
        this.customTableSuffix = customTableSuffix;
    }
}
