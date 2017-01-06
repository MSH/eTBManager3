package org.msh.etbm.commons.models.tableupdate;

import org.msh.etbm.commons.models.ModelException;
import org.msh.etbm.commons.models.data.TableColumn;
import org.msh.etbm.commons.models.data.TableColumnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by rmemoria on 4/1/17.
 */
@Service
public class TableSchemaService {

    @Autowired
    DataSource dataSource;

    @Value("${db.url:}")
    String dbUrl;


    public List<TableColumn> getTableSchema(String table) {
        List<TableColumn> lst = new ArrayList<>();

        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();

            ResultSet rs = metaData.getColumns(null, null, table, null);

            while (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                lst.add(createTableField(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ModelException(e);
        }

        return lst;
    }

    public void updateSchema(String tableName, List<FieldSchema> newSchema) {
        List<TableColumn> columns = getTableSchema(tableName);

        List<String> lst = new ArrayList<>();

        for (FieldSchema fs: newSchema) {
            String s = generateSqlAlterColumn(fs, columns);
            if (s != null) {
                lst.add(s);
            }
        }

        // check if there is no change to be performed
        if (lst.isEmpty()) {
            return;
        }

        StringBuilder s = new StringBuilder();

        s.append("ALTER TABLE ").append(tableName);

        for (String sql: lst) {
            s.append('\n').append(sql);
        }

        execAlterTable(s.toString());
    }


    /**
     * Execute the alter table instruction
     * @param sql
     */
    private void execAlterTable(String sql) {
        System.out.println("SQL = " + sql);
        JdbcTemplate templ = new JdbcTemplate(dataSource);
        templ.execute(sql);
    }

    private String generateSqlAlterColumn(FieldSchema fs, List<TableColumn> columns) {
        String newName = fs.getNewName();
        String oldName = fs.getSchema().getName();

        TableColumn colold = findColumn(columns, oldName);
        TableColumn colnew = newName != null ? findColumn(columns, newName) : null;

        // destination column doesn't exist ?
        if (colnew == null && colold == null) {
            return "ADD COLUMN " + newName + " " + sqlTypeName(fs.getSchema());
        }

        // the target column doesn't exist ?
        if (colnew == null) {
            return "CHANGE COLUMN " + oldName + " " + (newName != null ? newName : oldName) + " " +
                    sqlTypeName(fs.getSchema());
        }

        // if new column already exists and type is the same, do nothing
        if (!colnew.equalsTo(fs.getSchema())) {
            return "CHANGE COLUMN " + newName + " " + newName + " " + sqlTypeName(fs.getSchema());
        }

        return null;
    }

    private String sqlTypeName(TableColumn col) {
        return dbUrl.contains("hsqldb") ? hsqldbTypeName(col) : mysqlTypeName(col);
    }

    private String hsqldbTypeName(TableColumn col) {
        int size = col.getSize();

        switch (col.getType()) {
            case INT: return "INT";
            case BINARY: return "BINARY(" + size + ")";
            case VARCHAR: return "VARCHAR(" + size + ")";
            case BLOB: return "BLOB";
            case BOOL: return "BOOLEAN";
            case CHAR: return "CHAR(" + size + ")";
            case DATE: return "DATE";
            case LONG: return "BIGINT";
            case LONGTEXT: return "LONGTEXT";
            case TIMESTAMP: return "TIMESTAMP";
            default: throw new ModelException("Type not supported: " + col.getType());
        }
    }

    private String mysqlTypeName(TableColumn col) {
        int size = col.getSize();

        switch (col.getType()) {
            case INT: return "INT";
            case BINARY: return "BINARY(" + size + ")";
            case VARCHAR: return "VARCHAR(" + size + ")";
            case BLOB: return "LONGBLOB";
            case BOOL: return "BIT";
            case CHAR: return "CHAR(" + size + ")";
            case DATE: return "DATE";
            case LONG: return "BIGINT";
            case LONGTEXT: return "CLOB";
            case TIMESTAMP: return "DATETIME";
            default: throw new ModelException("Type not supported: " + col.getType());
        }
    }


    private TableColumn findColumn(List<TableColumn> columns, String fieldName) {
        Optional<TableColumn> res = columns
                .stream()
                .filter(it -> it.getName().equals(fieldName))
                .findFirst();

        return res.isPresent() ? res.get() : null;
    }


    private TableColumn createTableField(ResultSet rs) throws SQLException {
        String name = rs.getString("COLUMN_NAME");
        String stype = rs.getString("TYPE_NAME");
        TableColumnType type = convertType(stype);

        int size = type == TableColumnType.VARCHAR ? rs.getInt("COLUMN_SIZE") : 0;
        return new TableColumn(name, type, size);
    }

    private TableColumnType convertType(String s) {
        switch (s.toUpperCase()) {
            case "VARCHAR": return TableColumnType.VARCHAR;
            case "INT": return TableColumnType.INT;
            case "BIT": return TableColumnType.BOOL;
            case "DATE": return TableColumnType.DATE;
            case "BINARY": return TableColumnType.BINARY;
            default: throw new ModelException("Type not supported: " + s);
        }
    }
}
