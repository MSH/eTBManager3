package org.msh.etbm.services.cases.casemove;

import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.commons.entities.ServiceResult;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by Mauricio on 14/09/2016.
 */
@Service
public class OnTreatCaseMoveService {
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
    public TransferOutResponse transferOut(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());
        Unit unitTo = entityManager.find(Unit.class, req.getUnitToId());
        Date moveDate = req.getMoveDate();

        TreatmentHealthUnit currentTreatUnit = validateTransferOut(tbcase, unitTo, moveDate, req);

        // create the period of treatment for the new health unit
        Period newPeriod = new Period(DateUtils.incDays(moveDate, 1), tbcase.getTreatmentPeriod().getEndDate());
        Period prevPeriod = new Period(currentTreatUnit.getPeriod().getIniDate(), moveDate);

        // create new treatment health unit
        TreatmentHealthUnit newhu = new TreatmentHealthUnit();
        newhu.setPeriod(newPeriod);
        newhu.setTbcase(tbcase);
        newhu.setTransferring(true);
        tbcase.getTreatmentUnits().add(newhu);
        newhu.setTbunit((Tbunit)unitTo);

        currentTreatUnit.getPeriod().intersect(prevPeriod);

        splitPeriod(tbcase, newPeriod.getIniDate());

        tbcase.setTransferring(true);

        entityManager.persist(tbcase);

        return new TransferOutResponse(tbcase.getId(), tbcase.getDisplayString(), unitTo.getId());
    }

    private TreatmentHealthUnit validateTransferOut(TbCase tbcase, Unit unitTo, Date moveDate, CaseMoveRequest req) {
        // check state
        if (!tbcase.getState().equals(CaseState.ONTREATMENT)) {
            throw new EntityValidationException(tbcase, "state", "Case state should be On Treatment", null);
        }

        // get the current treatment unit
        TreatmentHealthUnit currentTreatUnit = findTransferOutTreatUnit(tbcase);
        if (currentTreatUnit == null) {
            throw new EntityNotFoundException();
        }

        // checks if date is before beginning treatment date
        if (!currentTreatUnit.getPeriod().isDateInside(moveDate)) {
            throw new EntityValidationException(req, "moveDate", null, "cases.move.errortreatdate");
        }

        // checks if units are different
        if (currentTreatUnit.getTbunit().equals(unitTo)) {
            throw new EntityValidationException(req, "unitTo", null, "cases.move.errorunit");
        }

        return currentTreatUnit;
    }

    /**
     * Roll back an on-going transfer from one unit to another, restoring the previous state before the transfer
     * @return
     */
    @Transactional
    public CaseActionResponse rollbackTransferOut(UUID tbcaseId) {
        TbCase tbcase = entityManager.find(TbCase.class, tbcaseId);
        if (tbcase.getTreatmentUnits().size() <= 1) {
            throw new EntityValidationException(null, null, "No unit to roll back transfer", null);
        }

        List<TreatmentHealthUnit> hus = tbcase.getSortedTreatmentHealthUnits();
        TreatmentHealthUnit tout = hus.get(hus.size() - 2);
        TreatmentHealthUnit tin = hus.get(hus.size() - 1);

        hus.remove(tin);
        entityManager.remove(tin);

        tout.getPeriod().setEndDate(tin.getPeriod().getEndDate());

        Date moveDate = tin.getPeriod().getIniDate();

        /*TODO: [MSANTOS] what to do now that this date field doesn't exists?
        Date iniContPhase = tbcase.getIniContinuousPhase();
        if ((iniContPhase != null) && (!iniContPhase.equals(movdt))) {
            prescribedMedicineHome.joinPeriods(movdt);
        }
        */

        tbcase.setState(CaseState.ONTREATMENT);
        entityManager.persist(tbcase);

        return new CaseActionResponse(tbcase.getId(), tbcase.getDisplayString());
    }

    /**
     * Register the transfer in of the case
     * @return
     */
    @Transactional
    public CaseActionResponse transferIn(CaseMoveRequest req) {
        TbCase tbcase = entityManager.find(TbCase.class, req.getTbcaseId());
        Date moveDate = req.getMoveDate();

        TreatmentHealthUnit prev = findTransferOutTreatUnit(tbcase);

        if (prev == null) {
            throw new EntityValidationException(req, null, "Previous unit not found. Notify the administrator.", null);
        }

        if (!moveDate.after(prev.getPeriod().getEndDate())) {
            throw new EntityValidationException(req, null, "Transfer date must be after the final treatment date of previous treatment unit.", null);
        }

        TreatmentHealthUnit hu = findTransferInHealthUnit(tbcase);
        if (!moveDate.before(hu.getPeriod().getEndDate())) {
            throw new EntityValidationException(req, null, "Transfer date must be before the final treatment date of the new treatment unit.", null);
        }

        hu.getPeriod().setIniDate(moveDate);
        hu.setTransferring(false);
        entityManager.persist(hu);

        tbcase.setState(CaseState.ONTREATMENT);
        entityManager.persist(tbcase);

        return new CaseActionResponse(tbcase.getId(), tbcase.getDisplayString());
    }

    private TreatmentHealthUnit findTransferInHealthUnit(TbCase tbcase) {
        for (TreatmentHealthUnit hu: tbcase.getTreatmentUnits()) {
            if (hu.isTransferring()) {
                return hu;
            }
        }
        return null;
    }

    /**
     * Returns the treatment health unit that is transferring out
     * @return
     */
    private TreatmentHealthUnit findTransferOutTreatUnit(TbCase tbcase) {
        Date dt = null;
        TreatmentHealthUnit aux = null;
        for (TreatmentHealthUnit hu: tbcase.getTreatmentUnits()) {
            if ((!hu.isTransferring()) && ((dt == null) || (hu.getPeriod().getIniDate().after(dt)))) {
                dt = hu.getPeriod().getIniDate();
                aux = hu;
            }
        }

        return aux;
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
            return ((unit != null) && (unit.getId().equals(uw.getUnit().getId())));
        }

        return true;
    }

    /** TODO: [MSANTOS] This method should not be here. Find a better archtecture.
     * Split the prescribed medicines periods in two based on the date parameters
     * @param dt
     */
    private void splitPeriod(TbCase tbcase, Date dt) {
        List<PrescribedMedicine> lstnew = new ArrayList<PrescribedMedicine>();

        for (PrescribedMedicine pm: tbcase.getPrescriptions()) {
            if ((pm.getPeriod().isDateInside(dt)) && (!pm.getPeriod().getIniDate().equals(dt))) {
                PrescribedMedicine aux = clonePrescribedMedicine(pm);

                pm.getPeriod().setEndDate( DateUtils.incDays(dt, -1) );
                aux.getPeriod().setIniDate( dt );

                // add to a temporary list not to interfere with the main loop
                lstnew.add(aux);
            }
        }

        for (PrescribedMedicine pm: lstnew) {
            tbcase.getPrescriptions().add(pm);
        }
    }

    /** TODO: [MSANTOS] This method should not be here. Find a better archtecture.
     * Create a clone of the prescribed medicine object
     * @param pm
     * @return
     */
    private PrescribedMedicine clonePrescribedMedicine(PrescribedMedicine pm) {
        PrescribedMedicine aux = new PrescribedMedicine();
        aux.setDoseUnit(pm.getDoseUnit());
        aux.setPeriod( new Period(pm.getPeriod()));
        aux.setFrequency(pm.getFrequency());
        aux.setProduct(pm.getProduct());
        aux.setTbcase(pm.getTbcase());
        return aux;
    }

    /** TODO: [MSANTOS] This method should not be here. Find a better archtecture.
     * Join two periods that were split in a date dt
     * @param dt date to join two adjacent periods
     */
    public void joinPeriods(TbCase tbcase, Date dt) {
        List<PrescribedMedicine> deletedMedicines = new ArrayList<>();

        for (PrescribedMedicine pm: tbcase.getPrescriptions()) {
            if (pm.getPeriod().getIniDate().equals(dt)) {
                PrescribedMedicine aux = findCompactibleLeftAdjacentPeriod(tbcase, pm);
                if (aux != null) {
                    pm.getPeriod().setIniDate(aux.getPeriod().getIniDate());
                    deletedMedicines.add(aux);
                }
            }
        }

        // commit deleted medicines
        if (deletedMedicines == null || deletedMedicines.size() < 1) {
            return;
        }

        for (PrescribedMedicine pm: deletedMedicines) {
            tbcase.getPrescriptions().remove(pm);
            if (entityManager.contains(pm)) {
                entityManager.remove(pm);
            }
        }
    }

    /** TODO: [MSANTOS] This method should not be here. Find a better archtecture.
     * Find a compatible left adjacent prescribed medicine of the prescribed medicine pm
     * @param pm
     * @return
     */
    private PrescribedMedicine findCompactibleLeftAdjacentPeriod(TbCase tbcase, PrescribedMedicine pm) {
        Date dt = DateUtils.incDays( pm.getPeriod().getIniDate(), -1);
        for (PrescribedMedicine aux: tbcase.getPrescriptions()) {
            if ((aux.getPeriod().getEndDate().equals(dt)) && (aux.getProduct().equals(pm.getProduct())) &&
                    (aux.getDoseUnit() == pm.getDoseUnit()) && (aux.getFrequency() == pm.getFrequency())) {
                return aux;
            }
        }
        return null;
    }
}
