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
 * Generate the initialization file
 *
 * Created by rmemoria on 8/11/16.
 */
@Service
public class SyncFileGenerator {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    DataSource dataSource;

    private List<SQLQueryBuilder> queries;

    private long count;

    public File generate(UUID unitId, Optional<Integer> initialVersion) throws IOException {
        if (queries == null) {
            initQueries(unitId, initialVersion.orElse(null));
        }

        File file = File.createTempFile("etbm", "inifile");

        JsonFactory jsonFactory = new JsonFactory();
        JsonGenerator generator = jsonFactory.createGenerator(file, JsonEncoding.UTF8);

        try {
            generateJsonContent(generator, initialVersion);
        } finally {
            generator.close();
        }


        return file;
    }


    /**
     * Generate the content of the sync file in JSON format
     * @param generator instance of the JsonGenerator (from the Jackson library)
     * @throws IOException
     */
    protected void generateJsonContent(JsonGenerator generator, Optional<Integer> initialVersion) throws IOException {
        generator.writeStartArray();

        TableChangesTraverser trav = new TableChangesTraverser(dataSource);

        for (SQLQueryBuilder qry: queries) {
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


    /**
     * Prepare the queries to return the records to generate the sync file
     * @param unitId
     * @param initialVersion
     */
    protected  synchronized void initQueries(UUID unitId, Integer initialVersion) {
        if (queries != null) {
            return;
        }

        Unit unit = entityManager.find(Unit.class, unitId);
        UUID wsId = unit.getWorkspace().getId();

        queryFrom("administrativeunit")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("unit")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("substance")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("source")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("product")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("medicine_substances")
                .join("product", "product.id = medicine_substances.medicine_id")
                .restrict("product.version > ?", initialVersion)
                .restrict("product.workspace_id = ?", wsId);

        queryFrom("agerange")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("regimen")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("medicineregimen")
                .join("regimen", "medicineregimen.regimen_id = regimen.id")
                .restrict("regimen.version > ?", initialVersion)
                .restrict("regimen.workspace_id = ?", wsId);

        queryFrom("tag")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("sys_user")
                .join("userworkspace", "userworkspace.user_id = sys_user.id")
                .restrict("userworkspace.unit_id = ?", unitId)
                .restrict("sys_user.version > ?", initialVersion);

        queryFrom("userprofile")
                .restrict("version > ?", initialVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("userpermission")
                .join("userprofile", "userprofile.id = userpermission.profile_id")
                .restrict("userprofile.version > ?", initialVersion);

        queryFrom("userworkspace")
                .restrict("version > ?", initialVersion)
                .restrict("unit_id = ?", unitId);

        // case module
        queryFrom("patient")
                .join("tbcase", "tbcase.patient_id = patient.id")
                .restrict("patient.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("tbcase")
                .restrict("version > ?", initialVersion)
                .restrict("owner_unit_id = ?", unitId);

        queryFrom("examculture")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("exammicroscopy")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examhiv")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examdst")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examxpert")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examxray")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("treatmenthealthunit")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("prescribedmedicine")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("prevtbtreatment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecontact")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casesideeffect")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("medicalexamination")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecomorbidities")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);
    }

    protected SQLQueryBuilder queryFrom(String table) {
        if (queries == null) {
            queries = new ArrayList<>();
        }

        SQLQueryBuilder qry = SQLQueryBuilder.from(table);
        qry.setDisableFieldAlias(true);
        qry.select(table + ".*");
        queries.add(qry);
        return qry;
    }
}
