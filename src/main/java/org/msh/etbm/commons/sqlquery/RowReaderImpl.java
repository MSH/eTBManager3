package org.msh.etbm.commons.sqlquery;

import org.msh.etbm.commons.objutils.ObjectUtils;

import java.sql.*;
import java.util.UUID;

/**
 * Implementation of {@link RowReader}, to give access to the values of the current row of a result set.
 *
 * Created by rmemoria on 22/8/16.
 */
public class RowReaderImpl implements RowReader {

    private ResultSet resultSet;
    private SQLQueryBuilder builder;

    RowReaderImpl(SQLQueryBuilder builder) {
        this.builder = builder;
    }

    protected SQLField findField(String fieldName) {
        SQLField field = builder.fieldByName(fieldName);

        if (field == null) {
            throw new SQLExecException("Field not found: " + fieldName);
        }

        return field;
    }

    @Override
    public String getString(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getString(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public Integer getInt(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getInt(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public UUID getUUID(String column) {
        SQLField field = findField(column);

        try {
            byte[] val = resultSet.getBytes(field.getIndex());
            if (val == null) {
                return null;
            }
            return ObjectUtils.bytesToUUID(val);
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public Boolean getBoolean(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getBoolean(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public byte[] getBytes(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getBytes(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public Date getDate(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getDate(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public Double getDouble(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getDouble(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public Time getTime(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getTime(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public Timestamp getTimestamp(String column) {
        SQLField field = findField(column);

        try {
            return resultSet.getTimestamp(field.getIndex());
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    @Override
    public <E extends Enum> E getEnum(String column, Class<E> enumClass) {
        SQLField field = findField(column);

        try {
            int index = resultSet.getInt(field.getIndex());
            if (resultSet.wasNull()) {
                return null;
            }
            Enum[] vals = enumClass.getEnumConstants();
            return (E)vals[index];
        } catch (SQLException e) {
            throw new SQLExecException(e);
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }
}
