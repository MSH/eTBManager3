package org.msh.etbm.services.cases;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.caseclose.CaseCloseResponse;
import org.msh.etbm.services.cases.caseclose.ReopenCaseResponse;
import org.msh.etbm.services.cases.cases.CaseData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Register log for case features
 * Created by msantos 22/07/2016.
 */
@Component
public class CaseLogHandler implements CommandLogHandler<Object, Object> {

    @Override
    public void prepareLog(CommandHistoryInput in, Object request, Object response) {

        switch (in.getType()) {
            case CommandTypes.CASES_CASE_CLOSE:
                prepareCaseCloseLog(in, request, (CaseCloseResponse)response);
                break;
            case CommandTypes.CASES_CASE_REOPEN:
                prepareReopenCaseLog(in, request, (ReopenCaseResponse) response);
                break;
        }

    }

    public void prepareCaseCloseLog(CommandHistoryInput in, Object request, CaseCloseResponse response) {
        in.setEntityId(response.getTbcaseId());
        in.setEntityName(response.getTbcaseDisplayString());
        in.setAction(CommandAction.EXEC);
        in.addItem("$TbCase.state", CaseState.CLOSED);

        in.addItem("$TbCase.outcomeDate", response.getOutcomeDate());
        in.addItem("$TbCase.outcome", response.getOutcome());

        if (response.getOutcome().equals("OTHER")) {
            in.addItem("$TbCase.otherOutcome", response.getOtherOutcome());
        }
    }

    public void prepareReopenCaseLog(CommandHistoryInput in, Object request, ReopenCaseResponse response) {
        in.setEntityId(response.getTbcaseId());
        in.setEntityName(response.getTbcaseDisplayString());
        in.setAction(CommandAction.EXEC);
        in.addItem("$TbCase.state", response.getState());
    }
}
