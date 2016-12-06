package org.msh.etbm.services.offline;

import java.util.Map;
import java.util.Set;

/**
 * Created by Mauricio on 02/12/2016.
 */
public class SQLCommandBuilder {

    private String insertCmd;
    private String updateCmd;
    private String deleteCmd;
    private String selectCmd;

    public SQLCommandBuilder(String tableName, Set<String> fields) {
        createInsertCommand(tableName, fields);
        createUpdateCommand(tableName, fields);
        createDeleteCommand(tableName);
        createSelectCommand(tableName);
    }

    private void createInsertCommand(String tableName, Set<String> fields) {
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

    private void createUpdateCommand(String tableName, Set<String> fields) {
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

    private void createDeleteCommand(String tableName) {
        String delete = "DELETE FROM $TABLENAME WHERE id = ?";
        delete = delete.replace("$TABLENAME", tableName);

        deleteCmd = delete;
    }

    private void createSelectCommand(String tableName) {
        String select = "SELECT id FROM $TABLENAME WHERE id = ?";
        select = select.replace("$TABLENAME", tableName);

        selectCmd = select;
    }

    public String getInsertCmd() {
        return insertCmd;
    }

    public String getUpdateCmd() {
        return updateCmd;
    }

    public String getDeleteCmd() {
        return deleteCmd;
    }

    public String getSelectCmd() {
        return selectCmd;
    }
}
