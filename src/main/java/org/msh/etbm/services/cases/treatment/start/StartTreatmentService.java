package org.msh.etbm.services.cases.treatment.start;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.CaseState;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

/**
 * Services available to start the case treatment
 *
 * Created by rmemoria on 31/8/16.
 */
@Service
public class StartTreatmentService {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Start the treatment using a standard regimen. In case of validation failure, an
     * {@link EntityValidationException} will be thrown
     * @param req The request containing the information about the standard regimen
     */
    @Transactional
    public void startStandardRegimen(StartStandardRegimenRequest req) {
        // check if is a valid case
        TbCase tbcase = entityManager.find(TbCase.class, req.getCaseId());
        if (tbcase == null) {
            throw new EntityValidationException(req, "caseId", Messages.NOT_VALID, null);
        }

        if (tbcase.getState() != CaseState.NOT_ONTREATMENT) {
            throw new EntityValidationException(req, "caseId", null,
                    "Case state is not valid. Must be open and not on treatment");
        }

        // check if it is a valid regimen
        Regimen regimen = entityManager.find(Regimen.class, req.getRegimenId());

        if (regimen == null || !regimen.isActive()) {
            throw new EntityValidationException(req, "regimenId", Messages.NOT_VALID, null);
        }
        int days = regimen.getDaysTreatment();
        if (days == 0) {
            throw new EntityValidationException(req, "regimenId", Messages.NOT_VALID, null);
        }

        // check if it is a valid unit
        Unit unit = req.getUnitId() != null ? entityManager.find(Unit.class, req.getUnitId()) : tbcase.getOwnerUnit();
        if (unit == null || !(unit instanceof Tbunit) || !unit.isActive()) {
            throw new EntityValidationException(req, "unitId", Messages.NOT_VALID, null);
        }

        startTreatmentRegimen(tbcase, (Tbunit)unit, regimen, req.getIniDate());
    }


    public void startInividualizedRegimen(StartIndividualizedRegimenRequest req) {
        // TODO Ricardo: Start inidvidualized regimen
    }

    /**
     * Start a case treatment for a standard regimen
     * @param tbcase
     * @param unit
     * @param regimen
     * @param iniDate
     */
    private void startTreatmentRegimen(TbCase tbcase, Tbunit unit, Regimen regimen, Date iniDate) {
        int days = regimen.getDaysTreatment();
        Date endDate = DateUtils.incDays(iniDate, days);

        // set case variables
        tbcase.setRegimen(regimen);
        tbcase.setState(CaseState.ONTREATMENT);
        tbcase.setTreatmentPeriod(new Period(iniDate, endDate));
        tbcase.setOwnerUnit(unit);

        // create treatment health unit
        TreatmentHealthUnit hu = new TreatmentHealthUnit();
        hu.setTbcase(tbcase);
        hu.setPeriod(new Period(iniDate, endDate));
        hu.setTbunit(unit);
        tbcase.getTreatmentUnits().add(hu);
        entityManager.persist(hu);

        // create list of prescribed medicines
        for (MedicineRegimen mr: regimen.getMedicines()) {
            // calculate the prescription period
            Date pini = DateUtils.incDays(iniDate, mr.getIniDay());
            Date pend = DateUtils.incDays(pini, mr.getDays());

            PrescribedMedicine pm = new PrescribedMedicine();
            pm.setPeriod(new Period(pini, pend));
            pm.setTbcase(tbcase);
            pm.setDoseUnit(mr.getDefaultDoseUnit());
            pm.setFrequency(mr.getDefaultFrequency());
            pm.setProduct(mr.getMedicine());

            tbcase.getPrescriptions().add(pm);
            entityManager.persist(pm);
        }

        entityManager.persist(tbcase);
        entityManager.flush();
    }
}
