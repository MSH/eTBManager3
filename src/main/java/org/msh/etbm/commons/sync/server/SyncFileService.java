package org.msh.etbm.commons.sync.server;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.db.entities.Unit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Generate the synchronization file from the server side
 *
 * Created by rmemoria on 8/11/16.
 */
@Service
public class SyncFileService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    private long count;

    /**
     * Generate a synchronization file from the server side
     * @param unitId
     * @param initialVersion
     * @return
     * @throws IOException
     */
    public File generate(UUID unitId, Optional<Integer> initialVersion) throws IOException {
        Unit unit = entityManager.find(Unit.class, unitId);
        TableQueryList queries = new TableQueryList(unit.getId(), unit.getWorkspace().getId(), initialVersion);

        File file = File.createTempFile("etbm", "inifile");

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);

        try {
            generateJsonContent(queries, generator, initialVersion);
        } finally {
            generator.close();
        }


        return file;
    }


    /**
     * Generate the content of the sync file in JSON format
     * @param queries The list of queries that will generate the sync file
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @param initialVersion The initial version to generate content from
     * @throws IOException
     */
    protected void generateJsonContent(TableQueryList queries, JsonGenerator generator, Optional<Integer> initialVersion) throws IOException {
        generator.writeStartArray();

        TableChangesTraverser trav = new TableChangesTraverser(dataSource);

        for (TableQueryItem item: queries.getList()) {
            SQLQueryBuilder qry = item.getQuery();

            trav.setQuery(qry);
            count = 0;
            trav.eachRecord((rec, index) -> generateJsonObject(generator, rec));

            System.out.println(qry.getMainTable() + "... Number of records -> " + count);

            trav.eachDeleted(initialVersion, id -> {
                System.out.println(id);
            });
        }

        generator.writeEndArray();
    }


    /**
     * Generate a Json object of a map
     * @param record the map containing field names and values
     */
    protected void generateJsonObject(JsonGenerator generator, Map<String, Object> record) {
        try {
            generator.writeStartObject();
            for (Map.Entry<String, Object> entry: record.entrySet()) {
                Object val = CompactibleJsonConverter.convertToJson(entry.getValue());
                generator.writeObjectField(entry.getKey(), val);
            }
            generator.writeEndObject();
        } catch (IOException e) {
            throw new SynchronizationException(e);
        }
        count++;
    }

}
