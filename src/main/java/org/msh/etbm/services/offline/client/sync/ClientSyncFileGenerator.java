package org.msh.etbm.services.offline.client.sync;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.services.admin.sysconfig.SysConfigData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.offline.CompactibleJsonConverter;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.client.sync.listeners.SyncFileGeneratorListener;
import org.msh.etbm.services.offline.filegen.TableChangesTraverser;
import org.msh.etbm.services.offline.filegen.TableQueryItem;
import org.msh.etbm.services.offline.filegen.TableQueryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
 * Generate the synchronization file from the client side
 *
 * Created by rmemoria on 8/11/16.
 */
@Service
public class ClientSyncFileGenerator {

    @Autowired
    DataSource dataSource;

    @Autowired
    SysConfigService sysConfigService;

    /**
     * Generate a synchronization file from the client side
     * @return the generated file
     * @throws SynchronizationException
     */
    @Async
    public void generate(UUID workspaceId, SyncFileGeneratorListener listener) throws SynchronizationException {
        try {
            File file = File.createTempFile("etbm", ".zip");

            FileOutputStream fout = new FileOutputStream(file);
            GZIPOutputStream zipOut = new GZIPOutputStream(fout);

            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator generator = jsonFactory.createGenerator(zipOut, JsonEncoding.UTF8);

            try {
                generateJsonContent(generator, workspaceId);
            } finally {
                generator.close();
                zipOut.close();
                fout.close();
            }

            listener.afterGenerate(file);

        } catch (IOException e) {
            throw new SynchronizationException(e);
        }
    }

    /**
     * Generate the content of the sync file in JSON format
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @throws IOException
     */
    protected void generateJsonContent(JsonGenerator generator, UUID workspaceId) throws IOException {
        SysConfigData data = sysConfigService.loadConfig();

        Integer version = data.getVersion();
        UUID unitId = data.getSyncUnit().getId();

        // get the list of tables to query
        ClientTableQueryList queries = new ClientTableQueryList(workspaceId, unitId);

        // start the file with an object
        generator.writeStartObject();

        // write reference version
        generator.writeObjectField("version", version);

        // write reference unit id
        generator.writeObjectField("sync-unit-id", CompactibleJsonConverter.convertToJson(unitId));

        // write the content of the table
        generator.writeFieldName("tables");
        writeTables(queries, unitId, generator);

        // end the file with an object
        generator.writeEndObject();
    }

    /**
     * Generate the content of the tables
     * @param queries The list of queries that will generate the sync file
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @throws IOException
     */
    protected void writeTables(TableQueryList queries, UUID unitId, JsonGenerator generator) throws IOException {
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
            trav.eachDeleted(Optional.empty(), unitId, true, id -> {
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
}
