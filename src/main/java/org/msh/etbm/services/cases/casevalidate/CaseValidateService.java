package org.msh.etbm.services.cases.casevalidate;

import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.CaseActionEvent;
import org.msh.etbm.services.cases.CaseActionResponse;
import org.msh.etbm.services.cases.CaseLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by Mauricio on 13/09/2016.
 */
@Service
public class CaseValidateService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    @Transactional
    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_VALIDATE)
    public CaseActionResponse validateCase(UUID tbcaseId) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);

        if (tbcase == null) {
            throw new EntityNotFoundException();
        }

        if (tbcase.isValidated()) {
            throw new EntityValidationException(tbcase, "validated", "Case is already validated", null);
        }

        if (tbcase.getState().equals(CaseState.CLOSED)) {
            throw new EntityValidationException(tbcase, "state", "Case must not be closed", null);
        }

        tbcase.setValidated(true);
        entityManager.persist(tbcase);
        entityManager.flush();

        CaseActionResponse res = new CaseActionResponse(tbcase.getId(), tbcase.getDisplayString());

        applicationContext.publishEvent(new CaseActionEvent(this, res));

        return res;
    }
}
