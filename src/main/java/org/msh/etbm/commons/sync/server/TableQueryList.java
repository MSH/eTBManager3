package org.msh.etbm.commons.sync.server;

import org.msh.etbm.commons.sqlquery.SQLQueryBuilder;

import java.util.*;

/**
 * Keep a list of all queries and the action to be taken when syncing in the client side
 *
 * Created by rmemoria on 14/11/16.
 */
public class TableQueryList {

    private UUID wsId;
    private UUID unitId;
    private Long initialVersion;
    private long finalVersion;

    private List<TableQueryItem> queries;

    public TableQueryList(UUID wsId, UUID unitId, Optional<Long> initialVersion, long finalVersion) {
        this.wsId = wsId;
        this.unitId = unitId;
        this.initialVersion = initialVersion.orElse(null);
        this.finalVersion = finalVersion;
    }


    /**
     * Return the list of items
     * @return
     */
    public List<TableQueryItem> getList() {
        if (queries == null) {
            initQueries();
        }

        return queries;
    }


    /**
     * Prepare the queries to return the records to generate the sync file
     */
    protected void initQueries() {

        queryFrom("countrystructure")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("administrativeunit")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        // ignore the fields that are self-reference
        queryFrom("unit",
                TableQueryItem.SyncAction.INSERT, Arrays.asList("supplier_id", "AUTHORIZERUNIT_ID"))
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        // include the self-reference fields to be updated
        queryFrom("unit", TableQueryItem.SyncAction.UPDATE, null)
                .select("id, supplier_id, authorizerunit_id")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("substance")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("source")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("product")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("medicine_substances")
                .join("product", "product.id = medicine_substances.medicine_id")
                .restrict("product.version > ?", initialVersion)
                .restrict("product.version < ?", finalVersion)
                .restrict("product.workspace_id = ?", wsId);

        queryFrom("agerange")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("regimen")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("medicineregimen")
                .join("regimen", "medicineregimen.regimen_id = regimen.id")
                .restrict("regimen.version > ?", initialVersion)
                .restrict("regimen.version < ?", finalVersion)
                .restrict("regimen.workspace_id = ?", wsId);

        queryFrom("tag")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("sys_user")
                .join("userworkspace", "userworkspace.user_id = sys_user.id")
                .restrict("userworkspace.unit_id = ?", unitId)
                .restrict("sys_user.version < ?", finalVersion)
                .restrict("sys_user.version > ?", initialVersion);

        queryFrom("userprofile")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        queryFrom("userpermission")
                .join("userprofile", "userprofile.id = userpermission.profile_id")
                .restrict("userprofile.version < ?", finalVersion)
                .restrict("userprofile.version > ?", initialVersion)
                .restrict("userprofile.workspace_id = ?", wsId);

        queryFrom("userworkspace")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("unit_id = ?", unitId);

        // TODO: [MSANTOS]: NAO EXISTIA, EU INCLUI, VERIFICAR COM RICARDO SE ESTA OK
        queryFrom("userworkspace_profiles")
                .join("userworkspace", "userworkspace.id = userworkspace_profiles.userworkspace_id")
                .restrict("userworkspace.version > ?", initialVersion)
                .restrict("userworkspace.version < ?", finalVersion)
                .restrict("userworkspace.unit_id = ?", unitId);

        queryFrom("report")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("workspace_id = ?", wsId);

        // case module
        queryFrom("patient")
                .join("tbcase", "tbcase.patient_id = patient.id")
                .restrict("patient.version > ?", initialVersion)
                .restrict("patient.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("tbcase")
                .restrict("version > ?", initialVersion)
                .restrict("version < ?", finalVersion)
                .restrict("owner_unit_id = ?", unitId);

        queryFrom("examculture")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("exammicroscopy")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examhiv")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examdst")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examxpert")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examxray")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("treatmenthealthunit")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("prescribedmedicine")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("prevtbtreatment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecontact")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casesideeffect")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("medicalexamination")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecomorbidities")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecomment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("issue")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("issuefollowup")
                .join("issue", "issue.id = $root.issue_id")
                .join("tbcase", "tbcase.id = issue.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("treatmentmonitoring")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.version > ?", initialVersion)
                .restrict("$root.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);
    }


    /**
     * Register a new query to be used in the synchronization file. The data from the query will be used to
     * update the records in the client side
     * @param table The name of the table
     * @return instance of {@link SQLQueryBuilder}
     */
    protected SQLQueryBuilder queryForUpdateFrom(String table) {
        return addTableInfo(table, TableQueryItem.SyncAction.UPDATE).getQuery();
    }

    /**
     * Register a new query to be used in the synchronization file. The data from the query will be used to
     * insert new records in the destination table in the client side
     * @param table
     * @return
     */
    protected SQLQueryBuilder queryFrom(String table) {
        return addTableInfo(table, TableQueryItem.SyncAction.INSERT).getQuery();
    }

    /**
     * Include a query with a list of fields to be ignored
     * @param table
     * @param ignoreList
     * @return
     */
    protected SQLQueryBuilder queryFrom(String table, TableQueryItem.SyncAction action, List<String> ignoreList) {
        TableQueryItem item = addTableInfo(table, action);
        item.setIgnoreList(ignoreList);

        return item.getQuery();
    }

    private TableQueryItem addTableInfo(String table, TableQueryItem.SyncAction action) {
        if (queries == null) {
            queries = new ArrayList<>();
        }

        SQLQueryBuilder qry = SQLQueryBuilder.from(table);
        qry.setDisableFieldAlias(true);
        qry.select(table + ".*");

        // TODO: [MSANTOS] verificar com ricardo se essa mudança está ok
        //TableQueryItem info = new TableQueryItem(qry, TableQueryItem.SyncAction.UPDATE);
        TableQueryItem info = new TableQueryItem(qry, action);

        queries.add(info);

        return info;
    }
}
