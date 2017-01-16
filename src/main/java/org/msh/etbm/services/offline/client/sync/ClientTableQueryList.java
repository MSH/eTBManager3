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

        // admin module
        queryFrom("tag")
                .restrict("synched = ?", notSynched)
                .restrict("workspace_id = ?", wsId);

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
