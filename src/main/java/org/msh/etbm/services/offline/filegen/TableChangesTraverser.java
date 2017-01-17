package org.msh.etbm.services.offline.filegen;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.lang.Object;
import java.util.*;

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

    /**
     * Traverse each deleted entity created in the table since the initial version (or not synched), or all records
     * @param version
     * @param unitId
     * @param isClient indicates if a client instance is using this component
     * @param trav
     * @return
     * @throws IOException
     */
    public TableChangesTraverser eachDeleted(Optional<Integer> version, UUID unitId, boolean isClient, DeletedRecordTraverseListener trav) throws IOException {

        // it is being used by a server instance to generate init file
        if (!isClient) {
            // if there is no initial version(initialization), so all records will be sent using eachNew
            if (!version.isPresent()) {
                return this;
            }

            traverseServerDeleted(version, unitId, trav);
        }

        // it is being used by a client instance to generate sync file
        if (isClient) {
            traverseClientDeleted(unitId, trav);
        }

        return this;
    }

    /**
     * Selects all relevant deleted entity, based on params, for a server instance during init or sync
     * @param version
     * @param unitId
     * @param trav
     * @throws IOException
     */
    protected void traverseServerDeleted(Optional<Integer> version, UUID unitId, DeletedRecordTraverseListener trav) throws IOException {
        String sql = "select tableId from deletedentity " +
                "where tableName like :tableName " +
                "and (unit_id is null or unit_id = :unitId) " +
                "and version > :version";

        Map<String, Object> args = new HashMap<>();
        args.put("tableName", queryBuilder.getTableName());
        args.put("unitId", ObjectUtils.uuidAsBytes(unitId));
        args.put("version", version.get());

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        List<Map<String, Object>> qryResults = jdbcTemplate.queryForList(sql, args);

        for (Map<String, Object> qryResult : qryResults) {
            trav.onDeletedRecord(ObjectUtils.bytesToUUID((byte[])qryResult.get("tableId")));
        }
    }

    /**
     * Selects all relevant deleted entity, based on params, for a client instance during sync
     * @param unitId
     * @param trav
     * @throws IOException
     */
    protected void traverseClientDeleted(UUID unitId, DeletedRecordTraverseListener trav) throws IOException {
        String sql = "select tableId from deletedentity " +
                "where tableName like :tableName " +
                "and (unit_id is null or unit_id = :unitId) " +
                "and synched = false";

        Map<String, Object> args = new HashMap<>();
        args.put("tableName", queryBuilder.getTableName());
        args.put("unitId", ObjectUtils.uuidAsBytes(unitId));

        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        List<Map<String, Object>> qryResults = jdbcTemplate.queryForList(sql, args);

        for (Map<String, Object> qryResult : qryResults) {
            trav.onDeletedRecord(ObjectUtils.bytesToUUID((byte[])qryResult.get("tableId")));
        }
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
