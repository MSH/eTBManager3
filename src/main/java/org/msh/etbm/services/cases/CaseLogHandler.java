package org.msh.etbm.services.cases;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.caseclose.CaseCloseResponse;
import org.msh.etbm.services.cases.caseclose.ReopenCaseResponse;
import org.msh.etbm.services.cases.tag.ManualCaseTagsResponse;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Register log for case features
 * Created by msantos 22/07/2016.
 */
@Component
public class CaseLogHandler implements CommandLogHandler<Object, CaseActionResponse> {

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public void prepareLog(CommandHistoryInput in, Object request, CaseActionResponse response) {
        in.setEntityId(response.getTbcaseId());
        in.setEntityName(response.getTbcaseDisplayString());
        in.setAction(CommandAction.EXEC);

        switch (in.getType()) {
            case CommandTypes.CASES_CASE_CLOSE:
                prepareCaseCloseLog(in, request, (CaseCloseResponse)response);
                break;
            case CommandTypes.CASES_CASE_REOPEN:
                prepareReopenCaseLog(in, request, (ReopenCaseResponse) response);
                break;
            case CommandTypes.CASES_CASE_TAG:
                prepareCaseTagsLog(in, request, (ManualCaseTagsResponse) response);
                break;
            case CommandTypes.CASES_CASE_VALIDATE:
                prepareCaseValidationLog(in, request, response);
                break;
        }

    }

    public void prepareCaseCloseLog(CommandHistoryInput in, Object request, CaseCloseResponse response) {
        in.addItem("$TbCase.state", CaseState.CLOSED);

        in.addItem("$TbCase.outcomeDate", response.getOutcomeDate());
        in.addItem("$TbCase.outcome", response.getOutcome());

        if (response.getOutcome().equals("OTHER")) {
            in.addItem("$TbCase.otherOutcome", response.getOtherOutcome());
        }
    }

    public void prepareReopenCaseLog(CommandHistoryInput in, Object request, ReopenCaseResponse response) {
        in.addItem("$TbCase.state", response.getState());
    }

    public void prepareCaseTagsLog(CommandHistoryInput in, Object request, ManualCaseTagsResponse response) {
        in.addDiff("$TbCase.manualtags", response.getPrevManualTags(), response.getNewManualTags());
    }

    public void prepareCaseValidationLog(CommandHistoryInput in, Object request, CaseActionResponse response) {
        // do nothing
    }
}
