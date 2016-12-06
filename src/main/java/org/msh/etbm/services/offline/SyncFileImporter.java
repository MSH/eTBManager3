package org.msh.etbm.services.offline;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mauricio on 28/11/2016.
 */
@Service
public class SyncFileImporter {

    @Autowired
    ImportRecordService db;

    public void importFile(File file, boolean compressed) {
        try {
            InputStream fileStream = new FileInputStream(file);
            // create a copy of downloaded file uncompressed
            if (compressed) {
                fileStream = new GZIPInputStream(fileStream);
            }

            // create streaming parser
            JsonFactory factory = new MappingJsonFactory();
            JsonParser parser = factory.createParser(fileStream);

            // start importing
            try {
                importData(parser);
            } finally {
                // close parser
                parser.close();
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void importData(JsonParser parser) throws IOException {

        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new SynchronizationException("Root should be object");
        }

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            // get field name that is being read
            String fieldName = parser.getCurrentName();

            // move from field name to field value
            parser.nextToken();

            Integer fileVersion = null;

            switch (fieldName) {
                case "version":
                    fileVersion = getVersion(parser);
                    break;
                case "workspace":
                    importWorkspace(parser);
                    break;
                case "config":
                    importConfig(parser, fileVersion);
                    break;
                case "tables":
                    importTables(parser);
                    break;
                default:
                    throw new SynchronizationException("Unprocessed field: " + fieldName);
            }
        }
    }

    private Integer getVersion(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        return node.asInt();
    }

    private void importWorkspace(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wmap = mapper.treeToValue(node, Map.class);

        // By default this field is false on offline instance
        wmap.put("sendSystemMessages", false);

        SQLCommandBuilder cmdBuilder = new SQLCommandBuilder("workspace", wmap.keySet());

        db.persist(cmdBuilder, wmap);
    }

    private void importConfig(JsonParser parser, Integer fileVersion) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> cmap = mapper.treeToValue(node, Map.class);

        // set fields for offline version
        cmap.put("id", 1);
        cmap.put("allowRegPage", false);
        cmap.put("clientMode", true);
        // TODO: [MSANTOS] set version

        SQLCommandBuilder cmdBuilder = new SQLCommandBuilder("systemconfig", cmap.keySet());

        db.persist(cmdBuilder, cmap);
    }

    private void importTables(JsonParser parser) throws IOException {
        if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
            throw new SynchronizationException("Expecting START_ARRAY. Check File layout.");
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node;
        String tableName;
        String action;

        while (parser.nextToken() != JsonToken.END_ARRAY) {
            // enter into the table object first parameter name (table name)
            parser.nextToken();
            // enter into the table object first parameter value (table name)
            parser.nextToken();

            // read tableName
            node = parser.readValueAsTree();
            tableName = node.asText();

            // enter into the table object second parameter name (action)
            parser.nextToken();
            // enter into the table object second parameter value (action)
            parser.nextToken();

            // read action
            node = parser.readValueAsTree();
            action = node.asText();

            // enter into the table object third parameter name (records)
            parser.nextToken();
            // enter into the table object third parameter value (records)
            parser.nextToken();

            SQLCommandBuilder cmdBuilder = null;
            // read records
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                node = parser.readValueAsTree();
                Map<String, Object> record = mapper.treeToValue(node, Map.class);

                if (cmdBuilder == null) {
                    cmdBuilder = new SQLCommandBuilder(tableName, record.keySet());
                }

                db.persist(action, cmdBuilder, record);
            }

            // enter into the table object fourth parameter name (deleted)
            parser.nextToken();
            // enter into the table object fourth parameter value (deleted)
            parser.nextToken();

            // read deleted
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                node = parser.readValueAsTree();
                Map<String, Object> record = mapper.treeToValue(node, Map.class);

                // TODO: [MSANTOS] executar no banco de dados
            }

            parser.nextToken();
        }
    }

    public static int stringToEnumOrdinal(Class enumClass, Object val) {
        if (!(val instanceof String)) {
            throw new RuntimeException("Value must be a String");
        }

        return ObjectUtils.stringToEnum((String)val, enumClass).ordinal();
    }
}
