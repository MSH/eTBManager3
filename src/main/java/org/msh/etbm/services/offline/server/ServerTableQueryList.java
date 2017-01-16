package org.msh.etbm.services.offline.server;

import org.msh.etbm.services.offline.filegen.TableQueryItem;
import org.msh.etbm.services.offline.filegen.TableQueryList;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

/**
 * Keep a list of all queries and the action to be taken when syncing in the client side
 *
 * Created by rmemoria on 14/11/16.
 */
public class ServerTableQueryList extends TableQueryList {

    private UUID wsId;
    private UUID unitId;
    private Integer initialVersion;
    private long finalVersion;

    public ServerTableQueryList(UUID wsId, UUID unitId, Optional<Integer> initialVersion, long finalVersion) {
        this.wsId = wsId;
        this.unitId = unitId;
        this.initialVersion = initialVersion.orElse(null);
        this.finalVersion = finalVersion;
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
                .restrict("sys_user.version < ?", finalVersion)
                .restrict("sys_user.version > ?", initialVersion)
                .restrict("exists(select * from userworkspace where userworkspace.user_id = sys_user.id and userworkspace.unit_id = ?)", unitId);

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
                .restrict("patient.version > ?", initialVersion)
                .restrict("patient.version < ?", finalVersion)
                .restrict("exists(select * from tbcase where tbcase.patient_id = patient.id and tbcase.owner_unit_id = ?)", unitId);

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

        queryFrom("tags_case")
                .join("tbcase", "tbcase.id = $root.case_id")
                .join("tag", "tag.id = $root.tag_id")
                .restrict("tag.sqlCondition is null")
                .restrict("tbcase.version > ?", initialVersion)
                .restrict("tbcase.version < ?", finalVersion)
                .restrict("tbcase.owner_unit_id = ?", unitId);
    }

    public UUID getUnitId() {
        return unitId;
    }

}
