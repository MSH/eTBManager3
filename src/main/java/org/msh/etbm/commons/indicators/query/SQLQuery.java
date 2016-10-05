package org.msh.etbm.commons.indicators.query;

import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

/**
 * Handle execution of SQL instructions using JDBC
 *
 * @author Ricardo Memoria
 *
 */
public class SQLQuery {

    private Integer maxResults;
    private Integer iniResult;

    /**
     * Execute the query and return an instance of the {@link DataTableImpl} with its content
     * @param dataSource The data source for the database
     * @param sql Query to be executed in a SQL format
     * @return {@link DataTableImpl} instance containing result of the query
     */
    public DataTableQuery execute(DataSource dataSource, String sql, Map<String, Object> parameters) {
        NamedParameterJdbcTemplate tmpl = new NamedParameterJdbcTemplate(dataSource);

        // create the DataTable that will receive result from query
        DataTableQuery tbl = new DataTableQueryImpl();

        String pagedSql = applyPagination(sql);

        // execute the SQL and read result
        tmpl.query(pagedSql, parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                fillRow(tbl, resultSet);
            }
        });

        return tbl;
    }

    /**
     * Apply pagination in the query result
     * @param sql instruction to implement pagination
     * @return the same SQL instruction with the MySQL pagination instruction implemented
     */
    private String applyPagination(String sql) {
        if (maxResults == null) {
            return sql;
        }

        sql += " limit " + (iniResult != null ? iniResult.toString() + "," : "") + maxResults.toString();
        return sql;
    }


    /**
     * Initialize the columns of the given {@link DataTableQuery} with the given meta data of the result set
     * @param tbl
     * @param rsmd
     * @throws SQLException
     */
    private void initDataTableColumns(DataTableQuery tbl, ResultSetMetaData rsmd) throws SQLException {
        int numCols = rsmd.getColumnCount();

        // initialize data table
        tbl.resize(numCols, 0);

        // create columns keys as name of the columns in the result set
        for (int i = 0; i < numCols; i++) {
            String colname = rsmd.getColumnName(i + 1);
            tbl.getQueryColumns().get(i).setFieldName(colname);
        }
    }

    /**
     * Fill a new row of a {@link }DataTableQuery} with the current row of the given ResultSet
     * @param tbl
     * @param rs
     * @throws SQLException
     */
    private void fillRow(DataTableQuery tbl, ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();

        // check if table was already initialized
        if (tbl.getColumnCount() == 0) {
            initDataTableColumns(tbl, rsmd);
        }

        // add values to a new row
        tbl.addRow();
        int r = tbl.getRowCount() - 1;
        for (int c = 0; c < rsmd.getColumnCount(); c++) {
            Object obj = rs.getObject(c + 1);
            tbl.setValue(c, r, obj);
        }
    }

    /**
     * Fill instance of {@link DataTableImpl} with content of {@link ResultSet} from the executed SQL query
     * @param tbl
     * @param rs
     * @throws SQLException
     */
    private void fillDataTable(DataTableQuery tbl, ResultSet rs) throws SQLException {
        // create columns
        ResultSetMetaData rsmd = rs.getMetaData();

        int numCols = rsmd.getColumnCount();

        // initialize data table
        tbl.resize(numCols, 0);

        // create columns keys as name of the columns in the result set
        for (int i = 0; i < numCols; i++) {
            String colname = rsmd.getColumnName(i + 1);
            tbl.getQueryColumns().get(i).setFieldName(colname);
        }

        // fill the data table
        while (rs.next()) {
            tbl.addRow();
            int r = tbl.getRowCount() - 1;
            for (int c = 0; c < rsmd.getColumnCount(); c++) {
                Object obj = rs.getObject(c + 1);
                tbl.setValue(c, r, obj);
            }
        }
    }


    /**
     * @return the maxResults
     */
    public Integer getMaxResults() {
        return maxResults;
    }


    /**
     * @param maxResults the maxResults to set
     */
    public void setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
    }


    /**
     * @return the iniResult
     */
    public Integer getIniResult() {
        return iniResult;
    }


    /**
     * @param iniResult the iniResult to set
     */
    public void setIniResult(Integer iniResult) {
        this.iniResult = iniResult;
    }
}
