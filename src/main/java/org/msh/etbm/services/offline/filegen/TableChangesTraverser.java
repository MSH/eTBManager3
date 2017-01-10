package org.msh.etbm.services.offline.filegen;

import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Traverse a table using the given instance of {@link SQLQueryBuilder}
 *
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
    public TableChangesTraverser eachRecord(RecordTraverseListener trav) throws IOException {
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
    protected void traverseAll(RecordTraverseListener trav) throws IOException {
        int index = 0;

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        queryBuilder.setMaxResult(PAGE_SIZE);

        while (true) {
            queryBuilder.setFirstResult(index);

            String sql = queryBuilder.generate();
            Map<String, Object> args = queryBuilder.getParameters();

            System.out.println(sql);
            System.out.println("params = " + args.toString());
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
