package org.msh.etbm.services.cases.treatment.start;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.CaseState;
import org.msh.etbm.services.admin.sysconfig.SysConfigData;
import org.msh.etbm.services.admin.sysconfig.SysConfigService;
import org.msh.etbm.services.cases.CaseActionEvent;
import org.msh.etbm.services.cases.treatment.TreatmentCmdLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Services available to start the case treatment
 *
 * Created by rmemoria on 31/8/16.
 */
@Service
public class StartTreatmentService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    SysConfigService sysConfigService;

    /**
     * Start a treatment based on the
     * @param req
     */
    @Transactional
    @CommandLog(type = CommandTypes.CASES_TREAT_INI, handler = TreatmentCmdLogHandler.class)
    public void startTreatment(@NotNull @Valid StartTreatmentRequest req) {
        // if it is a client instance, set the syncUnit as the start treatment unit
        // the user in client instance can't start treatment to another unit
        SysConfigData config = sysConfigService.loadConfig();
        if (config.isClientMode()) {
            req.setUnitId(config.getSyncUnit().getId());
        }

        // check if values were right informed to know if it is a standardized or individualized regimen
        if (req.getRegimenId() != null && req.getPrescriptions() != null && req.getPrescriptions().size() > 0) {
            throw new EntityValidationException(req, "regimenId", "Cannot provide both regimen and prescriptions to start a treatment", null);
        }

        if (req.getRegimenId() == null && (req.getPrescriptions() == null || req.getPrescriptions().size() == 0)) {
            throw new EntityValidationException(req, "regimenId", "Either the regimen or the prescriptions must be informed", null);
        }

        boolean isStandard = req.getRegimenId() != null;

        // check if is a valid case
        TbCase tbcase = entityManager.find(TbCase.class, req.getCaseId());
        if (tbcase == null) {
            throw new EntityValidationException(req, "caseId", null, Messages.NOT_VALID);
        }

        // check if it is a valid case state
        if (tbcase.getState() != CaseState.NOT_ONTREATMENT) {
            throw new EntityValidationException(req, "caseId", null,
                    "Case state is not valid. Must be open and not on treatment");
        }

        // check if it is a valid unit
        Unit unit = req.getUnitId() != null ? entityManager.find(Unit.class, req.getUnitId()) : tbcase.getOwnerUnit();
        if (unit == null || !(unit instanceof Tbunit) || !unit.isActive()) {
            throw new EntityValidationException(req, "unitId", Messages.NOT_VALID, null);
        }

        if (isStandard) {
            startStandardRegimen(req, tbcase, (Tbunit)unit);
        } else {
            startInividualizedRegimen(req, tbcase, (Tbunit)unit);
        }

        applicationContext.publishEvent(new CaseActionEvent(this, tbcase.getId(), tbcase.getDisplayString()));
    }

    /**
     * Start the treatment using a standard regimen. In case of validation failure, an
     * {@link EntityValidationException} will be thrown
     * @param req The request containing the information about the standard regimen
     * @param tbcase The TB case in use
     * @param unit The unit to start the treatment
     */
    protected void startStandardRegimen(StartTreatmentRequest req, TbCase tbcase, Tbunit unit) {
        // check if it is a valid regimen
        Regimen regimen = entityManager.find(Regimen.class, req.getRegimenId());

        if (regimen == null || !regimen.isActive()) {
            throw new EntityValidationException(req, "regimenId", Messages.NOT_VALID, null);
        }
        int days = regimen.getDaysTreatment();
        if (days == 0) {
            throw new EntityValidationException(req, "regimenId", Messages.NOT_VALID, null);
        }

        Date iniDate = req.getIniDate();
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


    /**
     * Start the individualized regimen for the given case in the given treatment unit
     * @param req The request containing information about the individualized regimen
     * @param tbcase The case where treatment will start in
     * @param unit The treatment unit to handle the treatment
     */
    protected void startInividualizedRegimen(StartTreatmentRequest req, TbCase tbcase, Tbunit unit) {
        int months = 0;
        // create list of prescribed medicines
        List<PrescribedMedicine> lst = new ArrayList<>();
        for (PrescriptionRequest presc: req.getPrescriptions()) {
            // get the length for this prescription (-1 because the initial month is 1 based)
            int len = presc.getMonths() + presc.getMonthIni() - 1;
            if (len > months) {
                months = len;
            }

            PrescribedMedicine pm = new PrescribedMedicine();
            Date ini = DateUtils.incMonths(req.getIniDate(), presc.getMonthIni() - 1);
            Date end = DateUtils.incMonths(ini, presc.getMonths());
            pm.setPeriod(new Period(ini, end));

            Product prod = entityManager.find(Product.class, presc.getProductId());
            pm.setProduct(prod);
            pm.setFrequency(presc.getFrequency());
            pm.setDoseUnit(presc.getDoseUnit());
            pm.setTbcase(tbcase);
            lst.add(pm);
        }
        tbcase.setPrescriptions(lst);

        // update data
        Date ini = req.getIniDate();
        Date end = DateUtils.incMonths(ini, months);
        tbcase.setTreatmentPeriod(new Period(ini, end));
        tbcase.setState(CaseState.ONTREATMENT);
        tbcase.setOwnerUnit(unit);

        TreatmentHealthUnit hu = new TreatmentHealthUnit();
        hu.setTbunit(unit);
        hu.setPeriod(new Period(ini, end));
        hu.setTbcase(tbcase);
        tbcase.getTreatmentUnits().add(hu);

    }
}
