package org.msh.etbm.commons.sync.server;

import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.db.enums.DatabaseOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Traverse a table using the synchronization log
 * Created by rmemoria on 8/11/16.
 */
public class TableChangesTraverser {

    // number of records returned in each query
    private static final int PAGE_SIZE = 100;

    private DataSource dataSource;
    private SQLQueryBuilder queryBuilder;

    public TableChangesTraverser(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    public TableChangesTraverser setQuery(SQLQueryBuilder builder) {
        this.queryBuilder = builder;
        return this;
    }

    /**
     * Traverse each new record created in the table since the initial version, or all records
     * if the version is not specified
     * @param trav
     * @return
     */
    public TableChangesTraverser eachRecord(RecordTraverseListener trav) {
        traverseAll(trav);

        return this;
    }

    public TableChangesTraverser eachDeleted(Optional<Integer> version, DeletedRecordTraverseListener trav) {
        // if there is no initial version, so all records will be sent using eachNew
        if (!version.isPresent()) {
            return this;
        }

        return this;
    }

    /**
     * Traverse all records for the given query paginating the result
     * @param trav the traverse function
     */
    protected void traverseAll(RecordTraverseListener trav) {
        int index = 0;

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        queryBuilder.setMaxResult(PAGE_SIZE);

        while (true) {
            queryBuilder.setFirstResult(index);

            String sql = queryBuilder.generate();
            Map<String, Object> args = queryBuilder.getParameters();

            List<Map<String, Object>> lst = jdbcTemplate.queryForList(sql, args);

            for (Map<String, Object> rec: lst) {
                trav.onRecord(rec, index);
                index++;
            }

            if (lst.size() < PAGE_SIZE) {
                return;
            }
        }
    }
}
