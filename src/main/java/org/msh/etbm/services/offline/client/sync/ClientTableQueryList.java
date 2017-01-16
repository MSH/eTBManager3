package org.msh.etbm.services.offline.client.sync;

import org.msh.etbm.services.offline.filegen.TableQueryList;
import org.msh.etbm.services.offline.filegen.TableQueryItem;

import java.util.*;

/**
 * Keep a list of all queries and the action to be taken when syncing in the server side
 *
 * Created by mauricio on 14/11/16.
 */
public class ClientTableQueryList extends TableQueryList {

    private UUID wsId;
    private UUID unitId;

    public ClientTableQueryList(UUID wsId, UUID unitId) {
        this.wsId = wsId;
        this.unitId = unitId;
    }

    /**
     * Prepare the queries to return the records to generate the sync file
     */
    protected void initQueries() {

        boolean notSynched = false;

        queryFrom("countrystructure")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("administrativeunit")
                .restrict("synched = ?", false)
                .restrict("workspace_id = ?", wsId);

        // ignore the fields that are self-reference
        queryFrom("unit",
                TableQueryItem.SyncAction.INSERT, Arrays.asList("supplier_id", "AUTHORIZERUNIT_ID"))
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        // include the self-reference fields to be updated
        queryFrom("unit", TableQueryItem.SyncAction.UPDATE, null)
                .select("id, supplier_id, authorizerunit_id")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("substance")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("source")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("product")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("medicine_substances")
                .join("product", "product.id = medicine_substances.medicine_id")
                .restrict("product.synched = ?", notSynched)
                .restrict("product.workspace_id = ?", wsId);

        queryFrom("agerange")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("regimen")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("medicineregimen")
                .join("regimen", "medicineregimen.regimen_id = regimen.id")
                .restrict("regimen.synched = ?", notSynched)
                .restrict("regimen.workspace_id = ?", wsId);

        queryFrom("tag")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("sys_user")
                .restrict("sys_user.synched = ?", notSynched)
                .restrict("exists(select * from userworkspace where userworkspace.user_id = sys_user.id and userworkspace.unit_id = ?)", unitId);

        queryFrom("userprofile")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        queryFrom("userpermission")
                .join("userprofile", "userprofile.id = userpermission.profile_id")
                .restrict("userprofile.synched = ?", notSynched)
                .restrict("userprofile.workspace_id = ?", wsId);

        queryFrom("userworkspace")
                .restrict("synched = ?", notSynched)
                .restrict("unit_id = ?", unitId);

        queryFrom("userworkspace_profiles")
                .join("userworkspace", "userworkspace.id = userworkspace_profiles.userworkspace_id")
                .restrict("userworkspace.synched = ?", notSynched)
                .restrict("userworkspace.unit_id = ?", unitId);

        queryFrom("report")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

        // case module
        queryFrom("patient")
                .restrict("synched = ?", notSynched)
                .restrict("exists(select * from tbcase where tbcase.patient_id = patient.id and tbcase.owner_unit_id = ?)", unitId);

        queryFrom("tbcase")
                .restrict("synched = ?", notSynched)
                .restrict("owner_unit_id = ?", unitId);

        queryFrom("examculture")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("exammicroscopy")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examhiv")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examdst")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examxpert")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("examxray")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("treatmenthealthunit")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("prescribedmedicine")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("prevtbtreatment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecontact")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casesideeffect")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("medicalexamination")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecomorbidities")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("casecomment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("issue")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("issuefollowup")
                .join("issue", "issue.id = $root.issue_id")
                .join("tbcase", "tbcase.id = issue.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("treatmentmonitoring")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);

        queryFrom("tags_case")
                .join("tbcase", "tbcase.id = $root.case_id")
                .join("tag", "tag.id = $root.tag_id")
                .restrict("tag.sqlCondition is null")
                .restrict("tbcase.synched = ?", notSynched)
                .restrict("tbcase.owner_unit_id = ?", unitId);
    }

}
