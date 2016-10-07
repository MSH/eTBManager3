package org.msh.etbm.services.cases.casemove;

import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.Tbunit;
import org.msh.etbm.db.entities.Unit;
import org.msh.etbm.db.entities.UserWorkspace;
import org.msh.etbm.services.cases.CaseActionEvent;
import org.msh.etbm.services.cases.CaseLogHandler;
import org.msh.etbm.services.security.ForbiddenException;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.UUID;

/**
 * Created by Mauricio on 12/09/2016.
 */
@Service
public class CaseMoveService {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    UserRequestService userRequestService;

    @Autowired
    CaseOnTreatMoveService caseOnTreatMoveService;

    @Autowired
    CaseNotOnTreatMoveService notOnTreatCaseMoveService;

    @Autowired
    ApplicationContext applicationContext;

    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_TRANSFER_OUT)
    public CaseMoveResponse transferOut(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());
        Unit unitTo = entityManager.find(Unit.class, req.getUnitToId());

        if (tbcase == null) {
            throw new EntityNotFoundException();
        }

        if (!(unitTo instanceof Tbunit)) {
            throw new EntityValidationException(unitTo, "DISCRIMINATOR", "Destiny unit must be a TbUnit", null);
        }

        if (!isWorkingUnit(tbcase)) {
            throw new ForbiddenException();
        }

        CaseMoveResponse res = null;

        switch (tbcase.getState()) {
            case NOT_ONTREATMENT:
                res = notOnTreatCaseMoveService.transferOut(req);
                break;
            case ONTREATMENT:
                if (req.getMoveDate() == null) {
                    throw new EntityValidationException(req, "moveDate", null, "javax.validation.constraints.NotNull.message");
                }
                res = caseOnTreatMoveService.transferOut(req);
                break;
            case CLOSED:
                throw new EntityValidationException(tbcase, "state", "Closed cases can't be transfered", null);
        }

        applicationContext.publishEvent(new CaseActionEvent(this, res));

        return res;
    }

    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_TRANSFER_ROLLBACK)
    public CaseMoveResponse rollbackTransferOut(UUID tbcaseId) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);

        if (tbcase == null) {
            throw new EntityNotFoundException();
        }

        if (!isWorkingUnit(tbcase)) {
            throw new ForbiddenException();
        }

        CaseMoveResponse res = null;

        switch (tbcase.getState()) {
            case NOT_ONTREATMENT:
                res = notOnTreatCaseMoveService.rollbackTransferOut(tbcaseId);
                break;
            case ONTREATMENT:
                res = caseOnTreatMoveService.rollbackTransferOut(tbcaseId);
                break;
            case CLOSED:
                throw new EntityValidationException(tbcase, "state", "Closed cases can't be transfered", null);
        }

        applicationContext.publishEvent(new CaseActionEvent(this, res));

        return res;
    }

    @CommandLog(handler = CaseLogHandler.class, type = CommandTypes.CASES_CASE_TRANSFER_IN)
    public CaseMoveResponse transferIn(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());

        if (tbcase == null) {
            throw new EntityNotFoundException();
        }

        if (!isWorkingUnit(tbcase)) {
            throw new ForbiddenException();
        }

        CaseMoveResponse res = null;

        switch (tbcase.getState()) {
            case NOT_ONTREATMENT:
                res = notOnTreatCaseMoveService.transferIn(req);
                break;
            case ONTREATMENT:
                if (req.getMoveDate() == null) {
                    throw new EntityValidationException(req, "moveDate", null, "javax.validation.constraints.NotNull.message");
                }
                res = caseOnTreatMoveService.transferIn(req);
                break;
            case CLOSED:
                throw new EntityValidationException(tbcase, "state", "Closed cases can't be transfered", null);
        }

        applicationContext.publishEvent(new CaseActionEvent(this, res));

        return res;
    }

    /** TODO: [MSANTOS] This method should not be here. Find a better archtecture.
     * Check if user is working on its working unit. It depends on the case state and the user profile.
     * 1) If user can play activities of all other units, so it's the working unit;
     * 2) If case is waiting for treatment, the user unit is compared to the case notification unit;
     * 3) If case is on treatment, the user unit is compared to the case treatment unit;
     * @return
     */
    private boolean isWorkingUnit(TbCase tbcase) {
        UserWorkspace uw = entityManager.find(UserWorkspace.class, userRequestService.getUserSession().getUserWorkspaceId());

        if (uw.isPlayOtherUnits()) {
            return true;
        }

        Tbunit treatmentUnit = tbcase.getOwnerUnit();

        if (treatmentUnit != null) {
            return (treatmentUnit.getId().equals(uw.getUnit().getId()));
        }

        Tbunit unit = tbcase.getNotificationUnit();
        if (unit != null) {
            return (unit.getId().equals(uw.getUnit().getId()));
        }

        return true;
    }
}
