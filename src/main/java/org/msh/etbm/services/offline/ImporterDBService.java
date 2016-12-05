package org.msh.etbm.services.offline;

import org.msh.etbm.commons.sync.server.CompactibleJsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by Mauricio on 02/12/2016.
 */
@Component
public class ImporterDBService {
    // TODO: trocar para ImportRecordService
    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    public void persist(String action, SQLCommandBuilder cmdBuilder, Map<String, Object> record, boolean convertParams) {
        // TODO: [MSANTOS] se config e workspace forem convertidos como as tabelas, o atributo converParams não é mais necessário
        String sql;

        TransactionTemplate txManager = new TransactionTemplate(platformTransactionManager);
        JdbcTemplate template = new JdbcTemplate(dataSource);

        switch (action) {
            case "INSERT":
                sql = recordExists() ? cmdBuilder.getUpdateCmd() : cmdBuilder.getInsertCmd();
                break;
            case "UPDATE":
                sql = cmdBuilder.getUpdateCmd();
                break;
            default:
                throw new RuntimeException("Unsupported action: " + action);
        }

        Object[] params;
        if (convertParams) {
            params = convertParams(record);
        } else {
            params = record.values().toArray();
        }

        txManager.execute(status -> {
            template.update(sql, params);
            return 0;
        });

    }

    public void delete(SQLCommandBuilder cmdBuilder, Object id) {
        // TODO: [MSANTOS] implement this
    }

    private boolean recordExists() {
        // TODO: [MSANTOS] implement this
        return false;
    }

    private Object[] convertParams(Map<String, Object> record) {
        Object[] ret = new Object[record.size()];

        int i = 0;
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            ret[i] = CompactibleJsonConverter.convertFromJson(entry.getValue());
            i++;
        }

        return ret;
    }
}
