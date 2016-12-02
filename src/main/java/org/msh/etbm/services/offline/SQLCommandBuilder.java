package org.msh.etbm.services.offline;

import java.util.Map;

/**
 * Created by Mauricio on 02/12/2016.
 */
public class SQLCommandBuilder {

    private String insertCmd;
    private String updateCmd;
    private String deleteCmd;

    public SQLCommandBuilder(String tableName, Map<String, Object> record) {
        createInsertCommand(tableName, record);
        createUpdateCommand(tableName, record);
        createDeleteCommand(tableName);
    }

    private void createInsertCommand(String tableName, Map<String, Object> record) {
        String insert = "INSERT INTO $TABLENAME($FIELD) VALUES($VALUE)";
        insert = insert.replace("$TABLENAME", tableName);

        for (Map.Entry<String, Object> entry : record.entrySet()) {
            insert = insert.replace("$FIELD", entry.getKey() + ", $FIELD");
            insert = insert.replace("$VALUE", "?, $VALUE");
        }

        insert = insert.replace(", $FIELD", "");
        insert = insert.replace(", $VALUE", "");

        insertCmd = insert;
    }

    private void createUpdateCommand(String tableName, Map<String, Object> record) {
        // TODO: implement this
    }

    private void createDeleteCommand(String tableName) {
        // TODO: implement this
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
