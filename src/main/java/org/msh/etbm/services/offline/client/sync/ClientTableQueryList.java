package org.msh.etbm.services.offline.client.sync;

import org.msh.etbm.services.offline.filegen.TableQueryList;

import java.util.UUID;

/**
 * Keep a list of all queries and the action to be taken when syncing in the server side
 *
 * Created by mauricio on 14/11/16.
 */
public class ClientTableQueryList extends TableQueryList {

    private UUID wsId;

    public ClientTableQueryList(UUID wsId) {
        this.wsId = wsId;
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
                .restrict("synched = ?", notSynched);

        queryFrom("tbcase")
                .restrict("synched = ?", notSynched);

        queryFrom("examculture")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("exammicroscopy")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("examhiv")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("examdst")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("examxpert")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("examxray")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("treatmenthealthunit")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("prescribedmedicine")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("prevtbtreatment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("casecontact")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("casesideeffect")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("medicalexamination")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("casecomorbidities")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("casecomment")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("issue")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("issuefollowup")
                .join("issue", "issue.id = $root.issue_id")
                .join("tbcase", "tbcase.id = issue.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("treatmentmonitoring")
                .join("tbcase", "tbcase.id = $root.case_id")
                .restrict("$root.synched = ?", notSynched);

        queryFrom("tags_case")
                .join("tbcase", "tbcase.id = $root.case_id")
                .join("tag", "tag.id = $root.tag_id")
                .restrict("tag.sqlCondition is null")
                .restrict("tbcase.synched = ?", notSynched);
    }

}
