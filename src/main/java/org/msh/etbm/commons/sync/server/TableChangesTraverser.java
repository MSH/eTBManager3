package org.msh.etbm.commons.sync.server;

import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.db.enums.DatabaseOperation;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Traverse a table using the synchronization log
 * Created by rmemoria on 8/11/16.
 */
public class TableChangesTraverser {

    // number of records returned in each query
    private static final int PAGE_SIZE = 100;

    private DataSource dataSource;
    private String tableName;
    private Integer initialVersion;
    private UUID unitId;

    public TableChangesTraverser(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    public TableChangesTraverser from(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public TableChangesTraverser setUnitId(UUID unitId) {
        this.unitId = unitId;
        return this;
    }

    public TableChangesTraverser setInitialVersion(int iniVersion) {
        this.initialVersion = iniVersion;
        return this;
    }

    /**
     * Traverse each new record created in the table since the initial version, or all records
     * if the version is not specified
     * @param trav
     * @return
     */
    public TableChangesTraverser eachNew(RecordTraverse trav) {
        validateFields();

        String sql = initialVersion != null ?
                "select * from " + tableName + " a" +
                "\ninner join syncdata b on b.tableId = a.id " +
                "\nwhere b.operation = " + DatabaseOperation.INSERT.ordinal() +
                " and b.id > " + initialVersion + " and a.owner_unit_id = ?" :
                "select * from " + tableName + " a where a.owner_unit_id = ?";

        traverseAll(sql, trav);

        return this;
    }

    public TableChangesTraverser eachUpdated(RecordTraverse trav) {
        validateFields();

        // if there is no initial version, send nothing, because it is supposed to send
        // all records using eachNew method
        if (initialVersion == null) {
            return this;
        }

        String sql = "select * from " + tableName + " a" +
                        "\ninner join syncdata b on b.tableId = a.id " +
                        "\nwhere b.operation = " + DatabaseOperation.UPDATE.ordinal() +
                        " and b.id > " + initialVersion + " and a.owner_unit_id = ?";

        traverseAll(sql, trav);

        return this;
    }

    public TableChangesTraverser eachDeleted(RecordTraverse trav) {
        // if there is no initial version, so all records will be sent using eachNew
        if (initialVersion == null) {
            return this;
        }

        return this;
    }

    /**
     * Traverse all records for the given query paginating the result
     * @param sql The query to run
     * @param trav the traverse function
     */
    protected void traverseAll(String sql, RecordTraverse trav) {
        int index = 0;

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        while (true) {
            String sqlLimit = sql + " limit " + PAGE_SIZE;

            if (index > 0) {
                sqlLimit += " offset " + index;
            }

            Object[] args = { unitId };
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

    public Integer getInitialVersion() {
        return initialVersion;
    }

    public String getTableName() {
        return tableName;
    }

    public UUID getUnitId() {
        return unitId;
    }

    protected void validateFields() {
        if (tableName == null) {
            throw new SynchronizationException("Table name must be informed");
        }

        if (unitId == null) {
            throw new SynchronizationException("Unit ID must be informed");
        }
    }
}
