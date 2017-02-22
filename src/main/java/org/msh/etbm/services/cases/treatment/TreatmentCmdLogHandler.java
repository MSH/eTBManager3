package org.msh.etbm.services.cases.treatment;

import org.msh.etbm.commons.commands.CommandAction;
import org.msh.etbm.commons.commands.CommandHistoryInput;
import org.msh.etbm.commons.commands.CommandLogHandler;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.treatment.followup.TreatFollowupUpdateRequest;
import org.msh.etbm.services.cases.treatment.start.StartTreatmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.UUID;

/**
 * Service responsible for the command registration log of treatment services
 *
 * Created by rmemoria on 25/8/16.
 */
@Service
public class TreatmentCmdLogHandler implements CommandLogHandler<Object, Object> {
    @Autowired
    EntityManager entityManager;


    @Override
    public void prepareLog(CommandHistoryInput in, Object request, Object response) {
        switch (in.getType()) {
            case CommandTypes.CASES_TREAT_FOLLOWUP:
                handleTreatmentFollowup(in, (TreatFollowupUpdateRequest)request);
                break;
            case CommandTypes.CASES_TREAT_UNDO:
                registerTreatmentUndo(in, (UUID)request);
                break;
            case CommandTypes.CASES_TREAT_INI:
                registerTreatmentIni(in, (StartTreatmentRequest)request);
                break;
        }
    }


    private void registerTreatmentIni(CommandHistoryInput in, StartTreatmentRequest request) {
        in.setAction(CommandAction.EXEC);
    }

    /**
     * Register the treatment followup
     * @param in
     * @param request
     */
    private void handleTreatmentFollowup(CommandHistoryInput in, TreatFollowupUpdateRequest request) {
        TbCase tbcase = entityManager.find(TbCase.class, request.getCaseId());

        in.setAction(CommandAction.EXEC);
        in.setEntityId(request.getCaseId());
        in.setEntityName(tbcase.getDisplayString());

        in.addItem("$period.monthyear", (request.getMonth() + 1) + "/" + request.getYear());
    }


    /**
     * Register in the command log the undo of the treatment
     * @param in the instance of {@link CommandHistoryInput} to log
     * @param caseId the ID of the case
     */
    private void registerTreatmentUndo(CommandHistoryInput in, UUID caseId) {
        TbCase tbcase = entityManager.find(TbCase.class, caseId);

        in.setEntityId(caseId);
        in.setEntityName(tbcase.getDisplayString());
        in.setAction(CommandAction.EXEC);
    }

}
