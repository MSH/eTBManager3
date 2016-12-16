package org.msh.etbm.services.sync.client.db;

import java.util.List;
import java.util.Set;

/**
 * Stores the SQL commands to be used on import service
 * Created by Mauricio on 02/12/2016.
 */
public class SQLCommandBuilder {

    private String insertCmd;
    private String updateCmd;
    private String deleteCmd;
    private String selectCmd;
    private List<String> deleteChildCommands;
    private String tableName;

    /**
     * When instantiated will create the insert, update, delete and select command used during the importing process.
     * @param tableName
     * @param fields
     */
    public SQLCommandBuilder(String tableName, Set<String> fields) {
        this.tableName = tableName;
        createInsertCommand(fields);
        createUpdateCommand(fields);
        createDeleteCommand();
        createSelectCommand();
    }

    /**
     * When instantiated will create the insert, update, delete and select command used during the importing process.
     * It will also get (if exists) the dependent commands for the table passed as parameter
     * @param tableName
     * @param fields
     */
    public SQLCommandBuilder(String tableName, Set<String> fields, SQLUpdateChildTables dependences) {
        this.tableName = tableName;
        createInsertCommand(fields);
        createUpdateCommand(fields);
        createDeleteCommand();
        createSelectCommand();
        deleteChildCommands = dependences.getParentCommands(tableName);
    }

    /**
     * Creates and stores in local variable the insert command used during the importing process.
     * @param fields
     */
    private void createInsertCommand(Set<String> fields) {
        String insert = "INSERT INTO $TABLENAME($FIELD) VALUES($VALUE)";
        insert = insert.replace("$TABLENAME", tableName);

        for (String field : fields) {
            insert = insert.replace("$FIELD", field + ", $FIELD");
            insert = insert.replace("$VALUE", "?, $VALUE");
        }

        insert = insert.replace(", $FIELD", "");
        insert = insert.replace(", $VALUE", "");

        insertCmd = insert;
    }

    /**
     * Creates and stores in local variable the update command used during the importing process.
     * @param fields
     */
    private void createUpdateCommand(Set<String> fields) {
        String update = "UPDATE $TABLENAME SET $FIELD = ?";
        update = update.replace("$TABLENAME", tableName);

        for (String field : fields) {
            if (!"id".equals(field)) {
                update = update.replace("$FIELD", field);
                update = update.concat(", $FIELD = ?");
            }
        }

        update = update.replace(", $FIELD = ?", "");

        update = update.concat(" WHERE id = ?");

        updateCmd = update;
    }

    /**
     * Creates and stores in local variable the delete command used during the importing process.
     */
    private void createDeleteCommand() {
        String delete = "DELETE FROM $TABLENAME WHERE id = ?";
        delete = delete.replace("$TABLENAME", tableName);

        deleteCmd = delete;
    }

    /**
     * Creates and stores in local variable the select command used during the importing process.
     */
    private void createSelectCommand() {
        String select = "SELECT id FROM $TABLENAME WHERE id = ?";
        select = select.replace("$TABLENAME", tableName);

        selectCmd = select;
    }

    /**
     *
     * @return the insert sql command that inserts a record from sync file
     */
    public String getInsertCmd() {
        return insertCmd;
    }

    /**
     *
     * @return the update sql command that updates a record from sync file
     */
    public String getUpdateCmd() {
        return updateCmd;
    }

    /**
     *
     * @return the update sql command that deletes a record from sync file
     */
    public String getDeleteCmd() {
        return deleteCmd;
    }

    /**
     *
     * @return the select command that checks if a record exists
     */
    public String getSelectCmd() {
        return selectCmd;
    }

    /**
     *
     * @return the list of SQL DELETE commands that delete dependent registers of this table.
     */
    public List<String> getDeleteChildCommands() {
        return deleteChildCommands;
    }

    /**
     *
     * @return table name
     */
    public String getTableName() {
        return tableName;
    }
}
