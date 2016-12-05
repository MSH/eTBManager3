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

    public SQLCommandBuilder(String tableName, Set<String> fields) {
        createInsertCommand(tableName, fields);
        createUpdateCommand(tableName, fields);
        createDeleteCommand(tableName);
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
        String update = "UPDATE $TABLENAME SET $FIELD = ? WHERE id = ?";
        update = update.replace("$TABLENAME", tableName);

        for (String field : fields) {
            update = update.replace("$FIELD", field);
            update = update.concat(", $FIELD = ?");
        }

        update = update.replace(", $FIELD = ?", "");

        updateCmd = update;
    }

    private void createDeleteCommand(String tableName) {
        String delete = "DELETE FROM $TABLENAME WHERE id = ?";
        delete = delete.replace("$TABLENAME", tableName);

        deleteCmd = delete;
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
}
