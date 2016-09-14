package org.msh.etbm.services.cases.casemove;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mauricio on 14/09/2016.
 */
@Service
public class NotOnTreatCaseMoveService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    UserRequestService userRequestService;

    // TODO: [MSANTOS] Missing implementation of commandlog
    // TODO: [MSANTOS] Missing implementation of ownerunit selection
    // TODO: [MSANTOS] Missing email dispatcher implementation

    /**
     * Execute the transfer of an on treat case to another health unit
     */
    @Transactional
    public ServiceResult transferOut(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());
        Unit unitTo = entityManager.find(Unit.class, req.getUnitToId());
        Date moveDate = req.getMoveDate();

        // check state
        if (!tbcase.getState().equals(CaseState.NOT_ONTREATMENT)) {
            throw new EntityValidationException(tbcase, "state", "Case state should be Not On Treatment", null);
        }

        // TODO
        return null;
    }

    /**
     * Roll back an on-going transfer from one unit to another, restoring the previous state before the transfer
     * @return
     */
    @Transactional
    public ServiceResult rollbackTransferOut(UUID tbcaseId) {
        // TODO
        return null;
    }

    /**
     * Register the transfer in of the case
     * @return
     */
    @Transactional
    public ServiceResult transferIn(CaseMoveRequest req) {
        // TODO
        return null;
    }
}
