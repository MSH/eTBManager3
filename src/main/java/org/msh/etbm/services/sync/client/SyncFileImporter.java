package org.msh.etbm.services.sync.client;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.services.session.search.SearchableCreator;
import org.msh.etbm.services.sync.CompactibleJsonConverter;
import org.msh.etbm.services.sync.SynchronizationException;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.msh.etbm.services.sync.client.db.RecordImporter;
import org.msh.etbm.services.sync.client.db.SQLCommandBuilder;
import org.msh.etbm.services.sync.client.db.SQLUpdateChildTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    RecordImporter db;

    @Autowired
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Autowired
    SearchableCreator searchableCreator;

    /**
     * If null importer is not running
     */
    private FileImportingPhase phase;

    /**
     * indicates which table is being imported
     */
    private String importingTable;

    /**
     * Imports sync file reading it as a stream.
     * @param file
     * @param compressed
     * @param parentServerUrl
     * @param initializing if true indicates that is importing an initialization file, if not, it is a synchronization file.
     */
    @Async
    public void importFile(File file, boolean compressed, String parentServerUrl, boolean initializing, FileImportListener listener) {
        if (phase != null) {
            throw new SynchronizationException("Importer is already running");
        }

        phase = FileImportingPhase.STARTING_IMPORTING;

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
                importData(parser, parentServerUrl);
            } finally {
                // close parser
                parser.close();
            }

            // update the relation of all auto generated tags
            phase = FileImportingPhase.UPDATING_TAGS;
            autoGenTagsCasesService.updateAllCaseTags();

        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            // notify service that importing has end
            listener.afterImport(file);

            // indicates that importer is not running anymore
            phase = null;
        }
    }

    /**
     * Runs the sync file calling the correct method that will persist the database changes.
     * @param parser
     * @throws IOException
     */
    private void importData(JsonParser parser, String parentServerUrl) throws IOException {

        if (parser.nextToken() != JsonToken.START_OBJECT) {
            throw new SynchronizationException("Root should be object");
        }

        phase =  FileImportingPhase.IMPORTING_TABLES;

        while (parser.nextToken() != JsonToken.END_OBJECT) {
            // get field name that is being read
            String fieldName = parser.getCurrentName();

            // move from field name to field value
            parser.nextToken();

            Integer fileVersion = null;

            switch (fieldName) {
                case "version":
                    importingTable = "version";
                    fileVersion = getVersion(parser);
                    break;
                case "workspace":
                    importingTable = "workspace";
                    importWorkspace(parser);
                    break;
                case "config":
                    importingTable = "systemconfig";
                    importConfig(parser, fileVersion, parentServerUrl);
                    break;
                case "tables":
                    importTables(parser);
                    break;
                default:
                    throw new SynchronizationException("Unprocessed field: " + fieldName);
            }
        }
    }

    /**
     * Returns the version from sync file.
     * @param parser
     * @return the version from sync file
     * @throws IOException
     */
    private Integer getVersion(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        return node.asInt();
    }

    /**
     * Convert and insert or update the workspace on database.
     * @param parser
     * @throws IOException
     */
    private void importWorkspace(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wmap = mapper.treeToValue(node, Map.class);

        // By default this field is false on offline instance
        wmap.put("sendSystemMessages", false);

        SQLCommandBuilder cmdBuilder = new SQLCommandBuilder("workspace", wmap.keySet());

        db.persist(cmdBuilder, wmap);
    }

    /**
     * Convert and insert or update the system config on database.
     * @param parser
     * @param fileVersion
     * @throws IOException
     */
    private void importConfig(JsonParser parser, Integer fileVersion, String parentServerUrl) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> cmap = mapper.treeToValue(node, Map.class);

        // set fields for offline version
        cmap.put("id", 1);
        cmap.put("allowRegPage", false);
        cmap.put("clientMode", true);
        cmap.put("version", fileVersion);
        // avoid setting it to null when synchronizing
        if (parentServerUrl != null) {
            cmap.put("serverURL", CompactibleJsonConverter.convertToJson(parentServerUrl));
        }

        SQLCommandBuilder cmdBuilder = new SQLCommandBuilder("systemconfig", cmap.keySet());

        db.persist(cmdBuilder, cmap);
    }

    /**
     * Runs the tables array from JSON sync file and insert or update each record.
     * Also deletes the deleted entities from this table JSON object.
     * @param parser
     * @throws IOException
     */
    private void importTables(JsonParser parser) throws IOException {
        if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
            throw new SynchronizationException("Expecting START_ARRAY. Check File layout.");
        }

        SQLUpdateChildTables dependences = new SQLUpdateChildTables();
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
            this.importingTable = tableName;

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
                    cmdBuilder = new SQLCommandBuilder(tableName, record.keySet(), dependences);
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

                // TODO: [MSANTOS] implement when developing sync
            }

            parser.nextToken();
        }

        this.importingTable = null;
    }

    /**
     * @return the phase of importing process, if null, importing is not running.
     */
    public FileImportingPhase getPhase() {
        return phase;
    }

    /**
     * @return the table that importing is importing.
     */
    public String getImportingTable() {
        return importingTable;
    }

    public enum FileImportingPhase {
        STARTING_IMPORTING,
        IMPORTING_TABLES,
        UPDATING_TAGS;
    }
}
