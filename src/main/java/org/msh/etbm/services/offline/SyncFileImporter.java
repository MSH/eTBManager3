package org.msh.etbm.services.offline;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.objutils.ObjectUtils;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.commons.sync.server.CompactibleJsonConverter;
import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mauricio on 28/11/2016.
 */
@Service
public class SyncFileImporter {

    @Autowired
    ImporterDBService db;

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

            switch (fieldName) {
                case "version":
                    importVersion(parser);
                    break;
                case "workspace":
                    importWorkspace(parser);
                    break;
                case "config":
                    importConfig(parser);
                    break;
                case "tables":
                    importTables(parser);
                    break;
                default:
                    throw new SynchronizationException("Unprocessed field: " + fieldName);
            }
        }
    }

    private void importVersion(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        Integer version = node.asInt();
        // TODO: [MSANTOS] fazer algo com essa info
    }

    private void importWorkspace(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wmap = mapper.treeToValue(node, Map.class);

        // convert id
        wmap.put("id", CompactibleJsonConverter.convertFromJson(wmap.get("id")));

        // convert enums
        wmap.put("patientNameComposition", stringToEnumOrdinal(NameComposition.class, wmap.get("patientNameComposition")));
        wmap.put("caseValidationTB", stringToEnumOrdinal(CaseValidationOption.class, wmap.get("caseValidationTB")));
        wmap.put("caseValidationDRTB", stringToEnumOrdinal(CaseValidationOption.class, wmap.get("caseValidationDRTB")));
        wmap.put("caseValidationNTM", stringToEnumOrdinal(CaseValidationOption.class, wmap.get("caseValidationNTM")));
        wmap.put("suspectCaseNumber", stringToEnumOrdinal(DisplayCaseNumber.class, wmap.get("suspectCaseNumber")));
        wmap.put("confirmedCaseNumber", stringToEnumOrdinal(DisplayCaseNumber.class, wmap.get("confirmedCaseNumber")));

        // By default this field is false on offline instance
        wmap.put("sendSystemMessages", false);

        // convert UUID to byte
        // TODO: [MSANTOS] a workspace poderia vir convertida assim como vem os outros registros, evitaria de ter que fazer essa conversão e as acima.
        // Pode padronizar
        wmap.put("id", ObjectUtils.uuidAsBytes((UUID) wmap.get("id")));

        SQLCommandBuilder cmdBuilder = new SQLCommandBuilder("workspace", wmap);

        db.persist("INSERT", cmdBuilder, wmap, false);
    }

    private void importConfig(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> cmap = mapper.treeToValue(node, Map.class);

        // set client mode config
        cmap.put("id", 1);
        cmap.put("allowRegPage", false);
        cmap.put("clientMode", true);
        // TODO: [MSANTOS] set version
        // TODO: [MSANTOS] alguns parametros nao estao sendo enviados, como id da workspace, verificar

        SQLCommandBuilder cmdBuilder = new SQLCommandBuilder("systemconfig", cmap);

        db.persist("INSERT", cmdBuilder, cmap, false);
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
                    cmdBuilder = new SQLCommandBuilder(tableName, record);
                }

                // TODO: [MSANTOS] faltando country structure. Coloquei, verificar com ricardo.
                db.persist(action, cmdBuilder, record, true);
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
