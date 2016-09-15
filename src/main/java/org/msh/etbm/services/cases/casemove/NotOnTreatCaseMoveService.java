package org.msh.etbm.services.cases.casemove;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.cases.CaseActionResponse;
import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.services.session.usersession.UserRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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

    /**
     * Execute the transfer of an on treat case to another health unit
     */
    @Transactional
    public CaseMoveResponse transferOut(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());
        Tbunit unitTo = entityManager.find(Tbunit.class, req.getUnitToId());

        // check state
        if (!tbcase.getState().equals(CaseState.NOT_ONTREATMENT)) {
            throw new EntityValidationException(tbcase, "state", "Case state should be Not On Treatment", null);
        }

        tbcase.setTransferring(true);
        tbcase.setTransferOutUnit(tbcase.getOwnerUnit());
        tbcase.setOwnerUnit(unitTo);

        entityManager.persist(tbcase);
        entityManager.flush();

        // create response
        CaseMoveResponse res = new CaseMoveResponse();
        res.setTbcaseId(tbcase.getId());
        res.setTbcaseDisplayString(tbcase.getDisplayString());
        res.setCurrentOwnerUnitName(tbcase.getOwnerUnit().getName());
        res.setCurrentOwnerUnitAU(tbcase.getOwnerUnit().getAddress().getAdminUnit().getFullDisplayName());
        res.setPreviousOwnerUnitName(tbcase.getTransferOutUnit().getName());
        res.setPreviousOwnerUnitAU(tbcase.getTransferOutUnit().getAddress().getAdminUnit().getFullDisplayName());

        return res;
    }

    /**
     * Roll back an on-going transfer from one unit to another, restoring the previous state before the transfer
     * @return
     */
    @Transactional
    public CaseMoveResponse rollbackTransferOut(UUID tbcaseId) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);

        tbcase.setOwnerUnit(tbcase.getTransferOutUnit());
        tbcase.setTransferOutUnit(null);
        tbcase.setTransferring(false);
        entityManager.persist(tbcase);

        CaseMoveResponse res = new CaseMoveResponse();
        res.setTbcaseId(tbcase.getId());
        res.setTbcaseDisplayString(tbcase.getDisplayString());
        res.setCurrentOwnerUnitName(tbcase.getOwnerUnit().getName());
        res.setCurrentOwnerUnitAU(tbcase.getOwnerUnit().getAddress().getAdminUnit().getFullDisplayName());

        return res;
    }

    /**
     * Register the transfer in of the case
     * @return
     */
    @Transactional
    public CaseMoveResponse transferIn(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());

        // create response
        CaseMoveResponse res = new CaseMoveResponse();
        res.setPreviousOwnerUnitName(tbcase.getTransferOutUnit().getName());
        res.setPreviousOwnerUnitAU(tbcase.getTransferOutUnit().getAddress().getAdminUnit().getFullDisplayName());
        res.setTbcaseId(tbcase.getId());
        res.setTbcaseDisplayString(tbcase.getDisplayString());
        res.setCurrentOwnerUnitName(tbcase.getOwnerUnit().getName());
        res.setCurrentOwnerUnitAU(tbcase.getOwnerUnit().getAddress().getAdminUnit().getFullDisplayName());

        tbcase.setTransferOutUnit(null);
        tbcase.setTransferring(false);
        entityManager.persist(tbcase);

        return res;
    }
}
