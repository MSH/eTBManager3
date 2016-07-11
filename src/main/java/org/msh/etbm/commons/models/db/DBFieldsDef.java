package org.msh.etbm.commons.models.db;

/**
 * Created by rmemoria on 7/7/16.
 */
public interface DBFieldsDef {

    DBFieldsDef add(String fieldName);

    DBFieldsDef join(String tableName, String on);

    String getRootTable();
}
