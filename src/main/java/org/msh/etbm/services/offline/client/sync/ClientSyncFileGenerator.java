package org.msh.etbm.services.offline.client.sync;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.offline.CompactibleJsonConverter;
import org.msh.etbm.services.offline.SynchronizationException;
import org.msh.etbm.services.offline.query.TableChangesTraverser;
import org.msh.etbm.services.offline.query.TableQueryItem;
import org.msh.etbm.services.offline.query.TableQueryList;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

/**
 * Generate the synchronization file from the client side
 *
 * Created by rmemoria on 8/11/16.
 */
@Service
public class ClientSyncFileGenerator {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    UserRequestService userRequestService;

    /**
     * Generate a synchronization file from the client side
     * @param unitId the ID of the unit to generate the file to
     * @return the generated file
     * @throws SynchronizationException
     */
    public File generate(UUID unitId) throws SynchronizationException {
        try {
            File file = File.createTempFile("etbm", ".zip");

            FileOutputStream fout = new FileOutputStream(file);
            GZIPOutputStream zipOut = new GZIPOutputStream(fout);

            JsonFactory jsonFactory = new JsonFactory();
            JsonGenerator generator = jsonFactory.createGenerator(zipOut, JsonEncoding.UTF8);

            try {
                generateJsonContent(unitId, generator);
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
     * @throws IOException
     */
    protected void generateJsonContent(UUID unitId, JsonGenerator generator) throws IOException {

        long version = sysConfigService.loadConfig().getVersion();

        Unit unit = entityManager.find(Unit.class, unitId);

        // get the list of tables to query
        ClientTableQueryList queries = new ClientTableQueryList(unit.getWorkspace().getId(),
                unit.getId());

        // start the file with an object
        generator.writeStartObject();

        // write reference version
        generator.writeObjectField("version", version);

        // write the content of the table
        generator.writeFieldName("tables");
        writeTables(queries, generator);

        // end the file with an object
        generator.writeEndObject();
    }

    /**
     * Generate the content of the tables
     * @param queries The list of queries that will generate the sync file
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @throws IOException
     */
    protected void writeTables(TableQueryList queries, JsonGenerator generator) throws IOException {
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

            /* TODO
            // write the deleted records (in an array of IDs)
            generator.writeFieldName("deleted");
            generator.writeStartArray();
            trav.eachDeleted(initialVersion, id -> {
                Object val = CompactibleJsonConverter.convertToJson(id);
                generator.writeObject(val);
            });
            generator.writeEndArray();*/

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
