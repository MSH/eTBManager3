package org.msh.etbm.services.offline.filegen;

import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;

import java.util.*;

/**
 * Created by Mauricio on 02/01/2017.
 */
public abstract class TableQueryList {

    private List<TableQueryItem> queries;

    /**
     * Return the list of items
     * @return
     */
    public List<TableQueryItem> getList() {
        if (queries == null) {
            initQueries();
        }

        return queries;
    }

    /**
     * Prepare the queries to return the records to generate the sync file
     */
    protected abstract void initQueries();

    /**
     * Register a new query to be used in the synchronization file. The data from the query will be used to
     * update the records in the client side
     * @param table The name of the table
     * @return instance of {@link SQLQueryBuilder}
     */
    protected SQLQueryBuilder queryForUpdateFrom(String table) {
        return addTableInfo(table, TableQueryItem.SyncAction.UPDATE).getQuery();
    }

    /**
     * Register a new query to be used in the synchronization file. The data from the query will be used to
     * insert new records in the destination table in the client side
     * @param table
     * @return
     */
    protected SQLQueryBuilder queryFrom(String table) {
        return addTableInfo(table, TableQueryItem.SyncAction.INSERT).getQuery();
    }

    /**
     * Include a query with a list of fields to be ignored
     * @param table
     * @param ignoreList
     * @return
     */
    protected SQLQueryBuilder queryFrom(String table, TableQueryItem.SyncAction action, List<String> ignoreList) {
        TableQueryItem item = addTableInfo(table, action);
        item.setIgnoreList(ignoreList);

        return item.getQuery();
    }

    private TableQueryItem addTableInfo(String table, TableQueryItem.SyncAction action) {
        if (queries == null) {
            queries = new ArrayList<>();
        }

        SQLQueryBuilder qry = SQLQueryBuilder.from(table);
        qry.setDisableFieldAlias(true);
        qry.select(table + ".*");

        TableQueryItem info = new TableQueryItem(qry, action);

        queries.add(info);

        return info;
    }
}
