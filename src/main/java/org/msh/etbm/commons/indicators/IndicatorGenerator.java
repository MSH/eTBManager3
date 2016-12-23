package org.msh.etbm.commons.indicators;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.filters.Filter;
import org.msh.etbm.commons.indicators.datatable.DataTable;
import org.msh.etbm.commons.indicators.datatable.DataTableUtils;
import org.msh.etbm.commons.indicators.datatable.impl.DataTableImpl;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTable;
import org.msh.etbm.commons.indicators.indicator.IndicatorDataTableImpl;
import org.msh.etbm.commons.indicators.query.DataTableQuery;
import org.msh.etbm.commons.indicators.query.IndicatorSqlBuilder;
import org.msh.etbm.commons.indicators.query.SQLQuery;
import org.msh.etbm.commons.indicators.tableoperations.ConcatTables;
import org.msh.etbm.commons.indicators.tableoperations.IndicatorTransform;
import org.msh.etbm.commons.indicators.tableoperations.KeyConverter;
import org.msh.etbm.commons.indicators.tableoperations.KeySorter;
import org.msh.etbm.commons.indicators.variables.Variable;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Generate an indicator report, consisting of variables compounding the rows and columns
 * @author Ricardo Memoria
 *
 */
@Service
public class IndicatorGenerator {

    /**
     * Number of records contained in the detailed report
     */
    private Long recordCount;

    @Autowired
    ApplicationContext applicationContext;

    @Value("${development:false}")
    boolean development;


    /**
     * Create the indicator result
     * @param req The instance of {@link IndicatorRequest} containing the information about
     *            the indicator to be produced
     * @return instance of {@link IndicatorDataTable} containing the indicator report
     */
    public IndicatorDataTable execute(IndicatorRequest req, DataSource dataSource, Messages messages) {
        initializeFilters(req.getFilterValues());

        IndicatorSqlBuilder builder = createSqlBuilder(req.getQueryBuilder());

        DataTable data = loadData(dataSource, req, builder);

        // no data was returned from the database ?
        if (data.getRowCount() == 0) {
            // return an empty data table
            return new IndicatorDataTableImpl();
        }

        KeySorter.sortByKey(data);

        IndicatorDataTable result = indicatorTransform(data, req.getColumnVariables(), req.getRowVariables());

        calcTotals(req, result, messages);

        return result;
    }


    protected void initializeFilters(Map<Filter, Object> filters) {
        for (Map.Entry<Filter, Object> entry: filters.entrySet()) {
            entry.getKey().initialize(applicationContext);
        }
    }

    /**
     * Return an instance of the {@link DataTableQuery} containing detailed list of cases
     *
     * @param req Request object with information to generate a detailed list of values from the indicator
     * @return instance of {@link DataTableQuery}
     */
    public DataTableQuery getDetailedReport(DataSource dataSource, DetailedIndicatorRequest req) {
        if (req.getQueryBuilder() == null) {
            throw new IndicatorException("The queryBuilder must be informed in order to generate indicators");
        }

        IndicatorSqlBuilder builder = createSqlBuilder(req.getQueryBuilder());

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
        if (req.getColumnVariables() != null) {
            for (Variable v : req.getColumnVariables()) {
                sqlBuilder.addVariable(v);
            }
        }

        if (req.getRowVariables() != null) {
            for (Variable v : req.getRowVariables()) {
                sqlBuilder.addVariable(v);
            }
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
        int num = var.getIterationCount();

        // consider at least 1 iteration for each variable
        if (num == 0) {
            num = 1;
        }

        for (int i = 0; i < num; i++) {
            sqlBuilder.setVariableIteration(var, i);

            // is the last item?
            if (varindex == sqlBuilder.getVariables().size() - 1) {
                DataTable tbl = createDataTableFromQuery(dataSource, sqlBuilder);
                convertDataToVariableKeys(tbl, target, sqlBuilder);
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

        if (development) {
            System.out.println(builder.createSql());
        }

        return qry.execute(dataSource, builder.createSql(), builder.getParameters());
    }


    /**
     * Create a new instance of a {@link IndicatorSqlBuilder} class
     *
     * @return
     */
    protected IndicatorSqlBuilder createSqlBuilder(SQLQueryBuilder queryBuilder) {
        return new IndicatorSqlBuilder(queryBuilder);
    }


    /**
     * Create a new data table already converted to the variable key
     *
     * @param source
     * @param dest
     * @param sqlBuilder
     * @return
     */
    protected void convertDataToVariableKeys(DataTable source, DataTable dest, IndicatorSqlBuilder sqlBuilder) {
        KeyConverter conv = new KeyConverter();
        conv.convertKeys(source, dest, sqlBuilder);
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

    protected void calcTotals(IndicatorRequest req, IndicatorDataTable tbl, Messages messages) {
        // check if columns can be totalized
        boolean totalEnabled = req.isColumnTotal() && !req.getColumnVariables()
                .stream().anyMatch(v -> !v.isTotalEnabled());

        // the key to store the information
        Object[] totalKey = { DataTableUtils.TOTAL };

        int columnCount = tbl.getColumnCount();

        // calculate the total for each row
        if (totalEnabled) {
            // add the key descriptor
            tbl.getColumnKeyDescriptors().get(0).put(DataTableUtils.TOTAL, messages.get("global.total"));

            List<Object[]> keys = tbl.getRowKeys();
            for (int r = 0; r < tbl.getRowCount(); r++) {
                List values = tbl.getRowValues(r);
                double total = values.stream().mapToDouble(w -> w instanceof Double ? (Double)w : 0).sum();
                tbl.setValue(totalKey, keys.get(r), total);
            }
        }

        // check if rows can be totalized
        totalEnabled = req.isRowTotal() && !req.getRowVariables()
                .stream().anyMatch(v -> !v.isTotalEnabled());

        double total = 0;

        // calculate the total for each column
        if (totalEnabled) {
            // add the key descriptor
            tbl.getRowKeyDescriptors().get(0).put(DataTableUtils.TOTAL, messages.get("global.total"));

            List<Object[]> keys = tbl.getColumnKeys();
            for (int c = 0; c < columnCount; c++) {
                double sum = 0;
                for (int r = 0; r < tbl.getRowCount(); r++) {
                    Object val = tbl.getValueByPosition(c, r);
                    if (val instanceof Number) {
                        sum += ((Number)val).doubleValue();
                    }
                }

                tbl.setValue(keys.get(c), totalKey, sum);
                total += sum;
            }
        }

        tbl.setValue(totalKey, totalKey, total);
    }

    /**
     * @return the recordCount
     */
    public Long getRecordCount() {
        return recordCount;
    }

}
