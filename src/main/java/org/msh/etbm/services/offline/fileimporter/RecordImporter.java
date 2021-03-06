package org.msh.etbm.services.offline.fileimporter;

import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.services.offline.CompactibleJsonConverter;
import org.msh.etbm.services.offline.SynchronizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service class that insert, update and deletes the workspace, system config and table records from the sync file
 * Created by Mauricio on 02/12/2016.
 */
@Component
public class RecordImporter {

    @Autowired
    DataSource dataSource;

    @Autowired
    PlatformTransactionManager platformTransactionManager;

    @Autowired
    ApplicationContext applicationContext;

    private String[] SYNC_TABLES = {"countrystructure", "administrativeunit", "unit", "substance", "source", "product",
            "agerange", "regimen", "tag", "sys_user", "userprofile", "userworkspace", "report", "patient", "tbcase",
            "examculture", "exammicroscopy", "examhiv", "examdst", "examxpert", "examxray", "treatmenthealthunit",
            "prescribedmedicine", "prevtbtreatment", "casecontact", "casesideeffect", "medicalexamination", "casecomorbidities",
            "casecomment", "issue", "issuefollowup", "treatmentmonitoring", "deletedentity"};

    /**
     * Persist workspace, systemconfig oor table records that comes inside the sync file.
     * If the action is INSERT the system will check if the record exists before executing an insert sql statement.
     * if exists, an update statement will be executed.
     * @param action
     * @param cmdBuilder
     * @param record
     */
    public void persist(String action, SQLCommandBuilder cmdBuilder, Map<String, Object> record) {

        // check if should update or insert the register
        String sql;
        List<String> dependentCommands;
        boolean isUpdate = false;

        if ("UPDATE".equals(action) || recordExists(cmdBuilder, getIdParam(record))) {

            isUpdate = true;
            sql = cmdBuilder.getUpdateCmd();
            dependentCommands = cmdBuilder.getDeleteChildCommands();

        } else if ("INSERT".equals(action)) {

            sql = cmdBuilder.getInsertCmd();
            dependentCommands = null;

        } else {
            throw new SynchronizationException("Unsupported action during record importer persist: " + action);
        }

        // execute commands
        TransactionTemplate txManager = new TransactionTemplate(platformTransactionManager);
        JdbcTemplate template = new JdbcTemplate(dataSource);

        Object[] params = getParams(record, isUpdate);
        Object idParam = getIdParam(record);

        txManager.execute(status -> {
            // Update/insert the current record
            template.update(sql, params);

            // Check if the table has any child and execute its dependent commands to delete all dependent registers, as
            // those dependent registered will be inserted forward on this importing.
            if (dependentCommands != null && idParam != null) {
                for (String sqlCommand : dependentCommands) {
                    template.update(sqlCommand, idParam);
                }
            }

            return 0;
        });

        // publish event about record inserting/updating
        RecordChangeEvent event = new RecordChangeEvent(cmdBuilder.getTableName(), idParam, isUpdate ? "UPDATE" : "INSERT");
        applicationContext.publishEvent(event);
    }

    /**
     * Persist workspace, systemconfig or table records that comes inside the sync file, setting 'INSERT' as action.
     * @param cmdBuilder
     * @param record
     */
    public void persist(SQLCommandBuilder cmdBuilder, Map<String, Object> record) {
        this.persist("INSERT", cmdBuilder, record);
    }

    /**
     * Detele an entity that comes inside deleted entity sections of sync file.
     * @param cmdBuilder
     * @param id
     */
    public void delete(SQLCommandBuilder cmdBuilder, UUID id) {
        // execute commands
        TransactionTemplate txManager = new TransactionTemplate(platformTransactionManager);
        JdbcTemplate template = new JdbcTemplate(dataSource);

        txManager.execute(status -> {
            try {
                // Delete the record
                template.update(cmdBuilder.getDeleteCmd(), ObjectUtils.uuidAsBytes(id));
            } catch (Exception e) {
                System.out.println("ERROR EXECUTING");
            }
            return 0;
        });

        // publish event about record deleting
        RecordChangeEvent event = new RecordChangeEvent(cmdBuilder.getTableName(), id, "DELETE");
        applicationContext.publishEvent(event);
    }

    /**
     * Checks if a already record exists on database.
     * @param cmdBuilder
     * @param id
     * @return true if exists and false if don't exists
     */
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

    /**
     * Copy the record values to a array of objects converting them from JSON to database format.
     * @param record
     * @param isUpdate
     * @return array of objects converted from JSON to database format
     */
    private Object[] getParams(Map<String, Object> record, boolean isUpdate) {
        // When it is an update command, id must be the first and last param
        if (isUpdate) {
            Object id = record.get("id");

            if (id == null) {
                id = record.get("ID");
            }

            if (id == null) {
                throw new SynchronizationException("Param id must not be null");
            }

            record.put("whereId", id);
        }

        Object[] ret = new Object[record.size()];

        int i = 0;
        for (Map.Entry<String, Object> entry : record.entrySet()) {
            ret[i] = CompactibleJsonConverter.convertFromJson(entry.getValue());
            i++;
        }

        return ret;
    }

    /**
     * Returns the id of the record converted to database format.
     * @param record
     * @return id of the record converted to database format
     */
    private Object getIdParam(Map<String, Object> record) {
        Object id = record.get("id");

        if (id == null) {
            id = record.get("ID");
        }

        if (id == null) {
            return null;
        }

        return CompactibleJsonConverter.convertFromJson(id);
    }

    /**
     * Sets all synchronizable records as synched
     */
    public void setAllAsSynched() {
        TransactionTemplate txManager = new TransactionTemplate(platformTransactionManager);
        JdbcTemplate template = new JdbcTemplate(dataSource);

        // Because of the triggers logic, to set as synched has to set as null, the trigger then will set as true
        String defaultQuery = "update $tableName set synched = null";

        //set all as synched
        for (String tableName : SYNC_TABLES) {
            String query = defaultQuery.replace("$tableName", tableName);

            txManager.execute(status -> {
                template.update(query);
                return 0;
            });
        }
    }
}
