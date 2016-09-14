package org.msh.etbm.services.cases.casevalidate;

import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.CaseLogHandler;
import org.msh.etbm.services.cases.CaseActionResponse;
import org.msh.etbm.services.cases.tag.AutoGenTagsCasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import javax.persistence.EntityManager;
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
    AutoGenTagsCasesService autoGenTagsCasesService;

    @Transactional
    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_VALIDATE)
    public CaseActionResponse validateCase(UUID tbcaseId){
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);

        if (tbcase == null) {
            // TODO: [MSANTOS] dessa maneira vai dar nullpointer, o primeiro parametro nao pode ser null
            throw new EntityValidationException(null, null, "Case not found", null);
        }

        if (tbcase.isValidated()) {
            // TODO: [MSANTOS] dessa maneira vai dar nullpointer, o primeiro parametro nao pode ser null
            throw new EntityValidationException(null, null, "Case is already validated", null);
        }

        if (tbcase.getState().equals(CaseState.CLOSED)) {
            // TODO: [MSANTOS] dessa maneira vai dar nullpointer, o primeiro parametro nao pode ser null
            throw new EntityValidationException(null, null, "Case must not be closed", null);
        }

        tbcase.setValidated(true);
        entityManager.persist(tbcase);
        entityManager.flush();

        //update case tags
        autoGenTagsCasesService.updateTags(tbcase.getId());

        return new CaseActionResponse(tbcase.getId(), tbcase.getDisplayString());
    }
}
