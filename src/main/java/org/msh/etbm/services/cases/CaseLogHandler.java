package org.msh.etbm.services.cases;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.cases.CaseData;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Register the log for close and reopen case
 * Created by msantos 22/07/2016.
 */
@Component
public class CaseLogHandler implements CommandLogHandler<Object, CaseData> {

    @Override
    public void prepareLog(CommandHistoryInput in, Object request, CaseData response) {
        /*
        TODOMS: implementar este handler para tratar pequenas funcionalidades dos casos e melhorar a gravação do log de close/reopen case
         */
        in.setEntityId(response.getId());
        in.setEntityName(response.getDisplayString());
        in.setAction(CommandAction.EXEC);
        in.addItem("$TbCase.state", response.getState());

        if (in.getType().equals(CommandTypes.CASES_CASE_CLOSE)) {
            in.addItem("$TbCase.outcomeDate", response.getOutcomeDate());
            in.addItem("$TbCase.outcome", response.getOutcome());

            if (response.getOutcome().equals("OTHER")) {
                in.addItem("$TbCase.otherOutcome", response.getOtherOutcome());
            }
        }
    }
}
