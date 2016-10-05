package org.msh.etbm.commons.indicators;

import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTableImpl;
import org.msh.etbm.commons.indicators.query.DataTableQuery;
import org.msh.etbm.commons.indicators.query.IndicatorSqlBuilder;
import org.msh.etbm.commons.indicators.query.SQLQuery;
import org.msh.etbm.commons.indicators.tableoperations.ConcatTables;
import org.msh.etbm.commons.indicators.tableoperations.IndicatorTransform;
import org.msh.etbm.commons.indicators.tableoperations.KeyConverter;
import org.msh.etbm.commons.indicators.variables.Variable;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generate an indicator report, consisting of variables compounding the rows and columns
 * @author Ricardo Memoria
 *
 */
public class IndicatorGenerator {

    private DataTable res1, res2;

    // new size after conversion
    private int rowsize;
    private int colsize;

    /**
     * Number of records contained in the detailed report
     */
    private Long recordCount;


    /**
     * Create the indicator result
     * @param req The instance of {@link IndicatorRequest} containing the information about
     *            the indicator to be produced
     * @return instance of {@link IndicatorDataTable} containing the indicator report
     */
    public IndicatorDataTable execute(DataSource dataSource, IndicatorRequest req) {
        IndicatorSqlBuilder builder = createSqlBuilder(req.getMainTable());

        DataTable data = loadData(dataSource, req, builder);
        res1 = data;

        // no data was returned from the database ?
        if (data.getRowCount() == 0) {
            // return an empty data table
            return new IndicatorDataTableImpl();
        }

        data = convertDataToVariableKeys(data, builder);
        res2 = data;

        colsize = calcVariablesSize(req.getColumnVariables());
        rowsize = calcVariablesSize(req.getRowVariables());

        IndicatorDataTable result = indicatorTransform(data, req.getColumnVariables(), req.getRowVariables());

        rowsize = 1;
        return result;
    }


    /**
     * Return an instance of the {@link DataTableQuery} containing detailed list of cases
     *
     * @param req Request object with information to generate a detailed list of values from the indicator
     * @return instance of {@link DataTableQuery}
     */
    public DataTableQuery getDetailedReport(DataSource dataSource, DetailedIndicatorRequest req) {
        IndicatorSqlBuilder builder = createSqlBuilder(req.getMainTable());

        Map<Filter, Object> filters = req.getFilterValues();

        for (Filter filter : filters.keySet()) {
            Object fvalue = filters.get(filter);
            filter.prepareFilterQuery(builder.getQueryDefs(), fvalue, null);
        }

        // prepare to get counting
        builder.getVariables().clear();

        // calculate record count
        DataTableQuery dt = createDataTableFromQuery(dataSource, builder);
        recordCount = (Long) dt.getValue(0, 0);

        // prepare to generate detailed report
        builder.setFieldList(req.getDetailedFields());

        builder.setOrderBy(req.getOrderBy());

        // limit the amount of records
        builder.setFirstResult(req.getFirstResult());
        builder.setMaxResult(req.getMaxResult());

        return createDataTableFromQuery(dataSource, builder);
    }

    /**
     * Load data from the data base
     */
    protected DataTableImpl loadData(DataSource dataSource, IndicatorRequest req, IndicatorSqlBuilder sqlBuilder) {
        // add variables to SQL builder
        for (Variable v : req.getColumnVariables()) {
            sqlBuilder.addVariable(v);
        }

        for (Variable v : req.getRowVariables()) {
            sqlBuilder.addVariable(v);
        }

        sqlBuilder.setFilters(req.getFilterValues());

        // create an empty table
        DataTableImpl tbl = new DataTableImpl();

        // run the iteration over all variables
        runVariableIteration(dataSource, tbl, sqlBuilder, 0);

        return tbl;
    }



    /**
     * Run recursively the sequence of iteration over the variables that have more than 1 iteration
     *
     * @param target     to do
     * @param sqlBuilder to do
     * @param varindex   to do
     */
    protected void runVariableIteration(DataSource dataSource, DataTable target, IndicatorSqlBuilder sqlBuilder, int varindex) {
        Variable var = sqlBuilder.getVariables().get(varindex);
        int num = var.getVariableOptions().getIterationCount();

        // consider at least 1 iteration for each variable
        if (num == 0) {
            num = 1;
        }

        for (int i = 0; i < num; i++) {
            sqlBuilder.setVariableIteration(var, i);

            // is the last item?
            if (varindex == sqlBuilder.getVariables().size() - 1) {
                DataTable tbl = createDataTableFromQuery(dataSource, sqlBuilder);
                ConcatTables.insertRows(target, tbl);
            } else {
                runVariableIteration(dataSource, target, sqlBuilder, varindex + 1);
            }
        }
    }


    /**
     * Execute the database query from the SQL builder and loads its result in a {@link DataTableImpl}
     * instance. If a variable is defined, it'll be called in the specific iteration. Usually, the
     * variable is defined just for iteration bigger than 1, since the iteration 1 is generally
     * called to all variables
     *
     * @param builder    the SQL builder that contains the variables and filters
     * @return instance of the {@link DataTableQuery} containing the result of the SQL executed in the server
     */
    protected DataTableQuery createDataTableFromQuery(DataSource dataSource, IndicatorSqlBuilder builder) {
        // load data
        SQLQuery qry = new SQLQuery();

        System.out.println(builder.createSql());

        return qry.execute(dataSource, builder.createSql(), builder.getParameters());
    }


    /**
     * Create a new instance of a {@link IndicatorSqlBuilder} class
     *
     * @return
     */
    protected IndicatorSqlBuilder createSqlBuilder(String mainTable) {
        return new IndicatorSqlBuilder(mainTable);
    }


    /**
     * Create a new data table already converted to the variable key
     *
     * @param sourcedt
     * @return
     */
    protected DataTable convertDataToVariableKeys(DataTable sourcedt, IndicatorSqlBuilder builder) {
        KeyConverter conv = new KeyConverter();

        // mount list of variables and columns
        List<Variable> vars = new ArrayList<Variable>();
        List<int[]> varcols = new ArrayList<int[]>();

        for (Variable var : builder.getVariables()) {
            vars.add(var);
            varcols.add(builder.getColumnsVariable(var));
        }

        DataTable dt = conv.execute(sourcedt, vars, varcols);

        return dt;
    }


    /**
     * @param lst
     * @return
     */
    protected int calcVariablesSize(List<Variable> lst) {
        int len = 0;
        for (Variable var : lst) {
            len += var.getVariableOptions().isGrouped() ? 2 : 1;
        }
        return len;
    }


    /**
     * Transform a data table into a cube using specific columns
     * to generate the new columns and rows.
     *
     * @param tbl        the table to be transformed
     * @param varColumns is the list of variables that compound the columns
     * @param varRows    is the list of variables that compound the rows
     * @return a new instance of the {@link IndicatorDataTable}
     */
    protected IndicatorDataTable indicatorTransform(DataTable tbl, List<Variable> varColumns, List<Variable> varRows) {
        IndicatorTransform trans = new IndicatorTransform();
        return trans.execute(tbl, varColumns, varRows, tbl.getColumnCount() - 1);
    }


    /**
     * @return the res1
     */
    public DataTable getRes1() {
        return res1;
    }


    /**
     * @return the res2
     */
    public DataTable getRes2() {
        return res2;
    }


    /**
     * Return the number of rows that the column header uses in the indicator table
     *
     * @return
     */
    public int getColumnHeaderSize() {
        return colsize;
    }

    /**
     * Return the number of columns that the row header uses in the indicator table
     *
     * @return
     */
    public int getRowHeaderSize() {
        return rowsize;
    }


    /**
     * @return the recordCount
     */
    public Long getRecordCount() {
        return recordCount;
    }

}
