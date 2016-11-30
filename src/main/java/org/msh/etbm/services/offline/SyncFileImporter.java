package org.msh.etbm.services.offline;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msh.etbm.commons.sync.SynchronizationException;
import org.msh.etbm.commons.sync.server.CompactibleJsonConverter;
import org.msh.etbm.db.entities.SystemConfig;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mauricio on 28/11/2016.
 */
@Component
public class SyncFileImporter {

    private static final int BUFFER_SIZE = 65535;

    public void importFile(File file, boolean compressed) throws IOException {
        try {
            File destFile;

            // create a copy of downloaded file uncompressed
            if (compressed) {
                destFile = File.createTempFile("temp", "etbm");
                uncompressFile(file, destFile);
            } else {
                destFile = file;
            }

            // create streaming parser
            JsonFactory factory = new MappingJsonFactory();
            JsonParser parser = factory.createParser(destFile);

            // start importing
            try {
                importData(parser);
            } finally {
                // delete temp file
                if (compressed && destFile != null) {
                    destFile.delete();
                }

                // close parser
                if (parser != null) {
                    parser.close();
                }
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
                    System.out.println("Unprocessed property: " + fieldName);
                    parser.skipChildren();
            }
        }
        
        System.out.println("Caboou");
    }

    private void importVersion(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        Integer version = node.asInt();
        System.out.println(version);
    }

    private void importWorkspace(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> wmap = mapper.treeToValue(node, Map.class);

        Workspace w = new Workspace();

        w.setId((UUID) CompactibleJsonConverter.convertFromJson(wmap.get("id")));
        w.setName((String) wmap.get("name"));
        w.setPatientNameComposition(stringToEnum(NameComposition.class, wmap.get("patientNameComposition")));
        w.setCaseValidationTB(stringToEnum(CaseValidationOption.class, wmap.get("caseValidationTB")));
        w.setCaseValidationDRTB(stringToEnum(CaseValidationOption.class, wmap.get("caseValidationDRTB")));
        w.setCaseValidationNTM(stringToEnum(CaseValidationOption.class, wmap.get("caseValidationNTM")));
        w.setSuspectCaseNumber(stringToEnum(DisplayCaseNumber.class, wmap.get("suspectCaseNumber")));
        w.setConfirmedCaseNumber(stringToEnum(DisplayCaseNumber.class, wmap.get("confirmedCaseNumber")));
        w.setMonthsToAlertExpiredMedicines((Integer) wmap.get("monthsToAlertExpiredMedicines"));
        w.setMinStockOnHand((Integer) wmap.get("minStockOnHand"));
        w.setMaxStockOnHand((Integer) wmap.get("maxStockOnHand"));

        System.out.println(w);
    }

    private void importConfig(JsonParser parser) throws IOException {
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> cmap = mapper.treeToValue(node, Map.class);

        SystemConfig config = new SystemConfig();

        config.setServerURL((String) cmap.get("serverURL"));
        config.setAdminMail((String) cmap.get("adminMail"));
        config.setUpdateSite((String) cmap.get("updateSite"));
        config.setUlaActive((boolean) cmap.get("ulaActive"));

        System.out.println(config);
    }

    private void importTables(JsonParser parser) throws IOException {
        if (parser.getCurrentToken() != JsonToken.START_ARRAY) {
            throw new SynchronizationException("Expecting START_ARRAY. Check File layout.");
        }

        // TODO: tem outro jeito de pegar esse Mapper?
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

            // read records
            int i = 0;
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                node = parser.readValueAsTree();
                Map<String, Object> record = mapper.treeToValue(node, Map.class);

                i++;
            }

            // enter into the table object fourth parameter name (deleted)
            parser.nextToken();
            // enter into the table object fourth parameter value (deleted)
            parser.nextToken();

            // read deleted
            int j = 0;
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                node = parser.readValueAsTree();
                Map<String, Object> record = mapper.treeToValue(node, Map.class);

                j++;
            }

            System.out.println(tableName + " " + action + " " + i + " records.");
            System.out.println(tableName + " " + action + " " + j + " deleted.");

            parser.nextToken();
        }
    }


    /**
     * Uncompress a file compressed with {@link GZIPInputStream}
     * @param gzipfile instance of {@link File} containing the compressed file
     * @param destfile instance of {@link File} where uncompressed file will be written to
     */
    private void uncompressFile(File gzipfile, File destfile) {
        try {
            if (destfile.exists()) {
                destfile.delete();
            }

            byte[] buffer = new byte[BUFFER_SIZE];

            GZIPInputStream gzin = new GZIPInputStream(new FileInputStream(gzipfile));
            FileOutputStream out = new FileOutputStream(destfile);

            int noRead;
            while ((noRead = gzin.read(buffer)) != -1) {
                out.write(buffer, 0, noRead);
            }
            gzin.close();
            out.close();

        } catch (Exception e) {
            throw new SynchronizationException("Error uncompressing the file");
        }

    }

    private <T> T stringToEnum(Class enumType, Object val) {
        if (!(val instanceof String)) {
            throw new RuntimeException("Value must be a String");
        }

        String s = (String) val;

        if (s == null || s.isEmpty()) {
            return null;
        }

        return (T) Enum.valueOf(enumType, s);
    }
}
