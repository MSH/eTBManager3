package org.msh.etbm.services.offline;

import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.commons.sync.server.CompactibleJsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by Mauricio on 02/12/2016.
 */
@Component
public class ImportRecordService {

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    public void persist(String action, SQLCommandBuilder cmdBuilder, Map<String, Object> record) {

        String sql = "";
        Object[] params = null;
        boolean isUpdate = false;

        // TODO: [MSANTOS] remove this try catch after finishing testing this
        try {

            TransactionTemplate txManager = new TransactionTemplate(platformTransactionManager);
            JdbcTemplate template = new JdbcTemplate(dataSource);

            switch (action) {
                case "INSERT":
                    if (recordExists(cmdBuilder, getIdParam(record))) {
                        sql = cmdBuilder.getUpdateCmd();
                        isUpdate = true;
                    } else {
                        sql = cmdBuilder.getInsertCmd();
                    }
                    break;
                case "UPDATE":
                    sql = cmdBuilder.getUpdateCmd();
                    isUpdate = true;
                    break;
                default:
                    throw new RuntimeException("Unsupported action: " + action);
            }

            params = getParams(record, isUpdate);

            final String finalSql = sql;
            final Object[] finalParams = params;

            txManager.execute(status -> {
                template.update(finalSql, finalParams);
                return 0;
            });

        } catch (Exception e) {
            System.out.println("Error executing sql");
            System.out.println(sql);
            if (params != null) {
                for (Object o : params) {
                    System.out.println(CompactibleJsonConverter.convertToJson(o));
                }
            }
            throw e;
        }

    }

    public void persist(SQLCommandBuilder cmdBuilder, Map<String, Object> record) {
        this.persist("INSERT", cmdBuilder, record);
    }

    public void delete(SQLCommandBuilder cmdBuilder, Object id) {
        // TODO: [MSANTOS] implement this
    }

    private boolean recordExists(SQLCommandBuilder cmdBuilder, Object id) {
        if (id == null) {
            return false;
        }

        TransactionTemplate txManager = new TransactionTemplate(platformTransactionManager);
        JdbcTemplate template = new JdbcTemplate(dataSource);

        List result = txManager.execute(status -> template.queryForList(cmdBuilder.getSelectCmd(), id));

        if (result != null && result.size() > 0) {
            return true;
        }

        return false;
    }

    private Object[] getParams(Map<String, Object> record, boolean isUpdate) {
        // When it is an update command, id must be the last param
        if (isUpdate) {
            Object id = record.remove("id");
            if (id == null) {
                throw new SynchronizationException("Param id must not be null");
            }
            record.put("id", id);
        }

        Object[] ret = new Object[record.size()];

        int i = 0;
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            ret[i] = CompactibleJsonConverter.convertFromJson(entry.getValue());
            i++;
        }

        return ret;
    }

    private Object getIdParam(Map<String, Object> record) {
        Object id = record.get("id");
        if (id == null) {
            return null;
        }

        return CompactibleJsonConverter.convertFromJson(id);
    }
}
