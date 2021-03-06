package org.msh.etbm.services.offline.server;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.services.admin.sysconfig.SysConfigData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.offline.CompactibleJsonConverter;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.filegen.TableChangesTraverser;
import org.msh.etbm.services.offline.filegen.TableQueryItem;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

/**
 * Generate the synchronization file from the server side
 *
 * Created by rmemoria on 8/11/16.
 */
@Service
public class ServerFileGenerator {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Generate a synchronization file from the server side
     * @param unitId the ID of the unit to generate the file to
     * @param workspaceId the ID of the workspace to generate the file to
     * @param initialVersion the initial version to generate just the differences
     * @return the generated file
     * @throws SynchronizationException
     */
    public File generate(UUID unitId, UUID workspaceId, Optional<Integer> initialVersion) throws SynchronizationException {
        try {
            File file = File.createTempFile("etbm", ".zip");

            FileOutputStream fout = new FileOutputStream(file);
            GZIPOutputStream zipOut = new GZIPOutputStream(fout);

            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator generator = jsonFactory.createGenerator(zipOut, JsonEncoding.UTF8);

            try {
                generateJsonContent(unitId, workspaceId, generator, initialVersion);
            } finally {
                generator.close();
                zipOut.close();
                fout.close();
            }

            return file;

        } catch (IOException e) {
            throw new SynchronizationException(e);
        }
    }

    /**
     * Generate the content of the sync file in JSON format
     * @param unitId the ID of the unit
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @param initialVersion The initial version to generate content from
     * @throws IOException
     */
    protected void generateJsonContent(UUID unitId, UUID workspaceId, JsonGenerator generator,
                                       Optional<Integer> initialVersion) throws IOException {

        long finalVersion = getCurrentVersion();

        Workspace workspace = entityManager.find(Workspace.class, workspaceId);

        // get the list of tables to query
        ServerTableQueryList queries = new ServerTableQueryList(workspaceId,
                unitId,
                initialVersion,
                finalVersion);

        // start the file with an object
        generator.writeStartObject();

        // write reference version
        generator.writeObjectField("version", finalVersion);

        // write reference unit id
        generator.writeObjectField("sync-unit-id", CompactibleJsonConverter.convertToJson(unitId));

        // write information about the workspace
        generator.writeFieldName("workspace");
        writeWorkspace(workspace, generator);

        // write the configuration
        generator.writeFieldName("config");
        writeConfig(generator);

        // write the content of the table
        generator.writeFieldName("tables");
        writeTables(queries, generator, initialVersion);

        // end the file with an object
        generator.writeEndObject();
    }


    /**
     * Write the content of the workspace to the JSON file
     * @param ws the workspace to write its content to
     * @param generator the JSON generator to write the content to JSON
     * @throws IOException
     */
    protected void writeWorkspace(Workspace ws, JsonGenerator generator) throws IOException {
        generator.writeStartObject();

        generator.writeObjectField("id", jsonValue(ObjectUtils.uuidAsBytes(ws.getId())));
        generator.writeObjectField("name", jsonValue(ws.getName()));
        generator.writeObjectField("patientNameComposition", jsonValue(ws.getPatientNameComposition()));
        generator.writeObjectField("caseValidationTB", jsonValue(ws.getCaseValidationTB()));
        generator.writeObjectField("caseValidationDRTB", jsonValue(ws.getCaseValidationDRTB()));
        generator.writeObjectField("caseValidationNTM", jsonValue(ws.getCaseValidationNTM()));
        generator.writeObjectField("suspectCaseNumber", jsonValue(ws.getSuspectCaseNumber()));
        generator.writeObjectField("confirmedCaseNumber", jsonValue(ws.getConfirmedCaseNumber()));
        generator.writeObjectField("monthsToAlertExpiredMedicines", jsonValue(ws.getMonthsToAlertExpiredMedicines()));
        generator.writeObjectField("minStockOnHand", jsonValue(ws.getMinStockOnHand()));
        generator.writeObjectField("maxStockOnHand", jsonValue(ws.getMaxStockOnHand()));

        generator.writeEndObject();
    }

    /**
     * Write the configuration data to the JSON file
     * @param generator the JSON generator to write the content to JSON
     * @throws IOException
     */
    protected void writeConfig(JsonGenerator generator) throws IOException {
        SysConfigData cfg = sysConfigService.loadConfig();

        generator.writeStartObject();

        generator.writeObjectField("serverURL", jsonValue(cfg.getServerURL()));
        generator.writeObjectField("adminMail", jsonValue(cfg.getAdminMail()));
        generator.writeObjectField("updateSite", jsonValue(cfg.getUpdateSite()));
        generator.writeObjectField("ulaActive", jsonValue(cfg.isUlaActive()));

        generator.writeEndObject();
    }

    /**
     * Invoke {@link CompactibleJsonConverter} to convert an object to a compatible
     * json value
     * @param value object to be converted to a json value
     * @return the object read to be serialized to JSON
     */
    private Object jsonValue(Object value) {
        return CompactibleJsonConverter.convertToJson(value);
    }

    /**
     * Generate the content of the tables
     * @param queries The list of queries that will generate the sync file
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @param initialVersion The initial version to generate content from
     * @throws IOException
     */
    protected void writeTables(ServerTableQueryList queries, JsonGenerator generator,
                               Optional<Integer> initialVersion) throws IOException {
        // start the array (main)
        generator.writeStartArray();

        TableChangesTraverser trav = new TableChangesTraverser(dataSource);

        for (TableQueryItem item: queries.getList()) {
            SQLQueryBuilder qry = item.getQuery();

            trav.setQuery(qry);

            // each element of the array is an object
            generator.writeStartObject();

            // write the table name
            generator.writeObjectField("table", qry.getTableName());

            generator.writeObjectField("action", item.getAction().toString());

            // write the records to include in the file (records are in an array)
            generator.writeFieldName("records");
            generator.writeStartArray();
            trav.eachRecord((rec, index) -> generateJsonObject(generator, rec, item.getIgnoreList()));
            generator.writeEndArray();

            // write the deleted records (in an array of IDs)
            generator.writeFieldName("deleted");
            generator.writeStartArray();
            trav.eachDeleted(initialVersion, queries.getUnitId(), false, id -> {
                Object val = CompactibleJsonConverter.convertToJson(id);
                generator.writeObject(val);
            });
            generator.writeEndArray();

            generator.writeEndObject();
        }

        // finish the array
        generator.writeEndArray();
    }


    /**
     * Generate a Json object of a map
     * @param record the map containing field names and values
     */
    protected void generateJsonObject(JsonGenerator generator, Map<String, Object> record, List<String> ignoreList) throws IOException {
        generator.writeStartObject();

        if (ignoreList != null) {
            for (String ignoreItem : ignoreList) {
                record.remove(ignoreItem);
            }
        }

        for (Map.Entry<String, Object> entry: record.entrySet()) {
            Object val = CompactibleJsonConverter.convertToJson(entry.getValue());
            generator.writeObjectField(entry.getKey(), val);
        }

        generator.writeEndObject();
    }


    /**
     * Return the current version used as a limit to avoid new records to corrupt the sync file
     * @return long value with the current version in use in the database
     */
    protected long getCurrentVersion() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        Long num = jdbcTemplate.queryForObject("select unix_timestamp() - 1000000000", null, Long.class);

        return num;
    }
}
