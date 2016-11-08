package org.msh.etbm.db.entities;

import org.msh.etbm.db.enums.DatabaseOperation;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Mauricio on 08/11/2016.
 */
@Entity
@Table(name = "synclog")
public class SyncLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Indicates the Synchronizable table where a record was created, updated or deleted
     */
    @Column(length = 100)
    @NotNull
    private String tableName;

    /**
     * Indicates the table id of the record that was created, updated or deleted
     */
    @NotNull
    private UUID tableId;

    /**
     * Indicates which database operation was executed (insert, update or delete)
     */
    @NotNull
    private DatabaseOperation operation;

    /**
     * The date and time that the trigger was executed
     */
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date execDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public UUID getTableId() {
        return tableId;
    }

    public void setTableId(UUID tableId) {
        this.tableId = tableId;
    }

    public DatabaseOperation getOperation() {
        return operation;
    }

    public void setOperation(DatabaseOperation operation) {
        this.operation = operation;
    }

    public Date getExecDate() {
        return execDate;
    }

    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }
}
