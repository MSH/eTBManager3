package org.msh.etbm.commons.sqlquery;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * Interface exposed in the {@link SQLQueryExec} to give access to the fields of the result set
 * Created by rmemoria on 22/8/16.
 */
public interface RowReader {

    String getString(String column);

    Integer getInt(String column);

    UUID getUUID(String column);

    Boolean getBoolean(String column);

    byte[] getBytes(String column);

    Date getDate(String column);

    Double getDouble(String column);

    Time getTime(String column);

    Timestamp getTimestamp(String column);

    <E extends Enum> E getEnum(String column, Class<E> enumClass);
}
