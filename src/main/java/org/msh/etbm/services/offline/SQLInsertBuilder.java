package org.msh.etbm.services.offline;

import java.util.Set;

/**
 * Created by Mauricio on 30/11/2016.
 */
public class SQLInsertBuilder implements SQLCommandBuilder {

    private String query = "INSERT INTO $TABLE_NAME($FIELD) VALUES ($VALUE)";

    public SQLInsertBuilder(String tableName, Set<String> fields) {
        if (tableName == null || tableName.isEmpty()) {
            throw new RuntimeException("Table name is required.");
        }

        if (fields == null || fields.size() < 2) {
            throw new RuntimeException("Fields are required and must have at least 2 items.");
        }

        initialize(tableName, fields);
    }

    private void initialize(String tableName, Set<String> fields) {
        // set table name
        query = query.replace("$TABLE_NAME", tableName);

        // set fields and values
        for (String field: fields) {
            query = query.replace("$FIELD", field + ", " + "$FIELD");
            query = query.replace("$VALUE", ":" + field + ", " + "$VALUE");
        }

        query = query.replace(", $FIELD", "");
        query = query.replace(", $VALUE", "");
    }

    public String getQuery() {
        return query;
    }
}
