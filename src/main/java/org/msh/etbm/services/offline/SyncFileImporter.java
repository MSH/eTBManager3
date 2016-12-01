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
import org.msh.etbm.db.entities.SystemConfig;
import org.msh.etbm.db.entities.Workspace;
import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

/**
 * Created by Mauricio on 28/11/2016.
 */
@Service
public class SyncFileImporter {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
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
        // TODO: fazer algo com essa info
    }

    private void importWorkspace(JsonParser parser) throws IOException {
        // TODO: salvar como sql nativo, se usar hibernate dá erro
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

        entityManager.persist(w);
    }

    private void importConfig(JsonParser parser) throws IOException {
        // TODO: salvar como sql nativo, se usar hibernate dá erro
        JsonNode node = parser.readValueAsTree();

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> cmap = mapper.treeToValue(node, Map.class);

        SystemConfig config = new SystemConfig();

        config.setServerURL((String) cmap.get("serverURL"));
        config.setAdminMail((String) cmap.get("adminMail"));
        config.setUpdateSite((String) cmap.get("updateSite"));
        config.setUlaActive((boolean) cmap.get("ulaActive"));

        entityManager.persist(config);
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

            // read records
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                node = parser.readValueAsTree();
                Map<String, Object> record = mapper.treeToValue(node, Map.class);

                // TODO: Usar JDBCTemplate
                // TODO: cada insert = 1 commit
                // TODO: se action é update, update direto, se for insert, select pra verificar se existe e então decide por insert ou update.
                // TODO: tomar como exemplo AutoGenTagsCasesService
                // TODO: criar uma classe para tratar insert/update/delete
            }

            // enter into the table object fourth parameter name (deleted)
            parser.nextToken();
            // enter into the table object fourth parameter value (deleted)
            parser.nextToken();

            // read deleted
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                node = parser.readValueAsTree();
                Map<String, Object> record = mapper.treeToValue(node, Map.class);

                // TODO: executar no banco de dados
            }

            parser.nextToken();
        }
    }

    public static <E extends Enum> E stringToEnum(Class<E> enumClass, Object val) {
        if (!(val instanceof String)) {
            throw new RuntimeException("Value must be a String");
        }

        return ObjectUtils.stringToEnum((String)val, enumClass);
    }
}
