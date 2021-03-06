package org.msh.etbm.services.cases.treatment.edit;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.*;
import org.msh.etbm.db.enums.MedicineLine;
import org.msh.etbm.services.cases.CaseActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service to make changes in the treatment regimen of a case
 * Created by rmemoria on 15/9/16.
 */
@Service
public class TreatmentEditService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    Messages messages;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * It will update only the endDate ate the first version. It will be evaluated if this functionality
     * should update anything else
     * @param req instance of {@link TreatmentUpdateRequest} containing the data to change
     */
    @Transactional
    // @CommandLog(type = CommandTypes.CASES_TREAT_EDIT, handler = TreatmentCmdLogHandler.class)
    public void updateTreatment(TreatmentUpdateRequest req) {
        Date newEndDate = req.getEndDate();

        // get and validate tbcase
        TbCase tbcase = entityManager.find(TbCase.class, req.getCaseId());
        if (tbcase == null) {
            throw new EntityNotFoundException("Tb case with ID " + req.getCaseId().toString() + " was not found");
        }

        // get and validate treatment period
        Period newTreatmentP = new Period(tbcase.getTreatmentPeriod().getIniDate(), newEndDate);
        if (newTreatmentP.isBroken() || !tbcase.getTreatmentPeriod().getIniDate().equals(newTreatmentP.getIniDate())) {
            throw new InvalidArgumentException("Treatment period is not valid");
        }

        // get latest healthunit
        TreatmentHealthUnit latestHU = getLatestTreatHU(tbcase);

        // get and validate treatment HU period
        Period newHealthUnitP = new Period(latestHU.getPeriod().getIniDate(), newEndDate);
        if (newHealthUnitP.isBroken() || !latestHU.getPeriod().getIniDate().equals(newHealthUnitP.getIniDate())) {
            throw new InvalidArgumentException("Treatment health unit period is not valid");
        }

        // update the prescriptions
        List<PrescribedMedicine> newPrescriptions = new ArrayList<>();
        for (PrescribedMedicine pm : tbcase.getPrescriptions()) {
            PrescribedMedicine newPm = clonePrescribedMedicine(pm);

            if (newPm.getPeriod().getIniDate().after(newEndDate)) {
                break;
            }

            // updates prescription period if newEndDate is inside this period or if prescription end date is equals old treatment end date
            if (newPm.getPeriod().isDateInside(newEndDate)
                    || newPm.getPeriod().getEndDate().equals(tbcase.getTreatmentPeriod().getEndDate())) {
                Period p = new Period(pm.getPeriod().getIniDate(), newEndDate);
                newPm.setPeriod(p);
            }

            newPrescriptions.add(newPm);
            entityManager.remove(pm);
        }
        tbcase.setPrescriptions(newPrescriptions);

        // update tbcase treatment period and health unit period
        tbcase.setTreatmentPeriod(newTreatmentP);
        latestHU.setPeriod(newHealthUnitP);

        entityManager.persist(tbcase);

        applicationContext.publishEvent(new CaseActionEvent(this, tbcase.getId(), tbcase.getDisplayString()));
    }

    /**
     * Add a prescription to the treatment regimen of a case
     * @param req
     */
    @Transactional
    // @CommandLog(type = CommandTypes.CASES_TREAT_ADDMED, handler = TreatmentCmdLogHandler.class)
    public void addPrescription(AddMedicineRequest req) {
        // get and validate tbcase
        TbCase tbcase = entityManager.find(TbCase.class, req.getCaseId());
        if (tbcase == null) {
            throw new EntityNotFoundException("TbCase with ID " + req.getCaseId().toString() + " was not found");
        }

        // get and validate product
        Product product = entityManager.find(Product.class, req.getProductId());
        if (product == null) {
            throw new EntityNotFoundException("Product with ID " + req.getCaseId().toString() + " was not found");
        }

        // get and validate period
        Period p = new Period(req.getIniDate(), req.getEndDate());
        if (p.isBroken()) {
            throw new InvalidArgumentException("Missing information on prescription period.");
        }

        Period treatPeriod = tbcase.getTreatmentPeriod();

        if (p.getIniDate().before(treatPeriod.getIniDate())) {
            String msg = messages.format(Messages.PERIOD_INIDATE_BEFORE, req.getIniDate());
            throw new EntityValidationException(req, "iniDate", msg, null);
        }

        PrescribedMedicine pm = new PrescribedMedicine();
        pm.setTbcase(tbcase);
        pm.setProduct(product);
        pm.setPeriod(p);
        pm.setDoseUnit(req.getDoseUnit());
        pm.setFrequency(req.getFrequency());
        pm.setComments(req.getComments());

        checkPrescriptionsInterceptions(pm, tbcase);
        tbcase.getPrescriptions().add(pm);

        // check if regimen is now individualized
        tbcase.setMovedToIndividualized(regimenMovedToIndivid(tbcase));

        checkTreatPeriod(tbcase);
        entityManager.persist(tbcase);

        applicationContext.publishEvent(new CaseActionEvent(this, tbcase.getId(), tbcase.getDisplayString()));
    }

    /**
     * Update a prescription of the treatment regimen
     * @param req
     */
    @Transactional
    // @CommandLog(type = CommandTypes.CASES_TREAT_PRESCEDT, handler = TreatmentCmdLogHandler.class)
    public void updatePrescription(PrescriptionUpdateRequest req) {
        // get and validate prescription
        PrescribedMedicine pm = entityManager.find(PrescribedMedicine.class, req.getPrescriptionId());
        if (pm == null) {
            throw new EntityNotFoundException("Prescription with ID " + req.getPrescriptionId().toString() + " was not found");
        }

        // get tbcase
        TbCase tbcase = pm.getTbcase();

        // get and validate period
        Period p = new Period(req.getIniDate(), req.getEndDate());
        if (p.isBroken()) {
            throw new InvalidArgumentException("Prescription period is not valid");
        }

        Period treatPeriod = tbcase.getTreatmentPeriod();

        if (p.getIniDate().before(treatPeriod.getIniDate())) {
            String msg = messages.format(Messages.PERIOD_INIDATE_BEFORE, req.getIniDate());
            throw new EntityValidationException(req, "iniDate", msg, null);
        }

        // should preserve previous prescription?
        if (req.isPreservePrevPeriod() && !pm.getPeriod().isInside(p) && !pm.getPeriod().equals(p)) {
            // only preserves it, if the old prescribed medicine period is not inside the new period
            // The old period will be adjusted when calling checkPrescriptionsInterceptions method
            PrescribedMedicine aux = clonePrescribedMedicine(pm);
            tbcase.getPrescriptions().add(aux);
        }

        pm.setPeriod(p);
        pm.setDoseUnit(req.getDoseUnit());
        pm.setFrequency(req.getFrequency());
        pm.setComments(req.getComments());

        checkPrescriptionsInterceptions(pm, tbcase);

        // check if regimen is now individualized
        tbcase.setMovedToIndividualized(regimenMovedToIndivid(tbcase));

        checkTreatPeriod(tbcase);
        entityManager.persist(tbcase);

        applicationContext.publishEvent(new CaseActionEvent(this, tbcase.getId(), tbcase.getDisplayString()));
    }

    @Transactional
    // @CommandLog
    public void removePrescription(UUID prescriptionId) {

        PrescribedMedicine pm = entityManager.find(PrescribedMedicine.class, prescriptionId);
        if (pm == null) {
            throw new EntityNotFoundException("Prescription with ID " + prescriptionId.toString() + " was not found");
        }

        TbCase tbcase = pm.getTbcase();
        tbcase.getPrescriptions().remove(pm);

        // check if regimen is now individualized
        tbcase.setMovedToIndividualized(regimenMovedToIndivid(tbcase));

        checkTreatPeriod(tbcase);
        entityManager.remove(pm);
        entityManager.persist(tbcase);

        applicationContext.publishEvent(new CaseActionEvent(this, tbcase.getId(), tbcase.getDisplayString()));
    }

    /**
     * Checks if treatment endDate should be updated, based on new prescriptions. IniDate will not be modified.
     * @param tbcase modified tbcase
     */
    private void checkTreatPeriod(TbCase tbcase) {
        if (tbcase.getPrescriptions() == null || tbcase.getPrescriptions().isEmpty() ||
                tbcase.getTreatmentUnits() == null || tbcase.getTreatmentUnits().isEmpty()) {
            return;
        }

        // get latest prescription
        PrescribedMedicine latestPrescription = tbcase.getPrescriptions().get(0);

        for (PrescribedMedicine pm : tbcase.getPrescriptions()) {
            if (latestPrescription.getPeriod().getEndDate().before(pm.getPeriod().getEndDate())) {
                latestPrescription = pm;
            }
        }

        // get latest health unit
        TreatmentHealthUnit latestTreatHU = getLatestTreatHU(tbcase);

        // get latest prescription date
        Date latestPrescDate = latestPrescription.getPeriod().getEndDate();

        // check treatment period of the tbcase
        if (latestPrescDate.compareTo(tbcase.getTreatmentPeriod().getEndDate()) != 0) {
            tbcase.getTreatmentPeriod().setEndDate(latestPrescDate);
        }

        // check treatment period of the treatment health unit
        if (latestPrescDate.compareTo(latestTreatHU.getPeriod().getEndDate()) != 0) {
            latestTreatHU.getPeriod().setEndDate(latestPrescDate);
        }
    }

    /**
     * Update same product prescriptions period if the new prescription period intercepts other prescription period.
     * @param mainPresc Prescription that will not have its period changed
     * @param tbcase modified tbcase
     */
    private void checkPrescriptionsInterceptions(PrescribedMedicine mainPresc, TbCase tbcase) {
        List<PrescribedMedicine> sameProdList = new ArrayList<>();

        for (PrescribedMedicine pm : tbcase.getPrescriptions()) {
            if (pm.getProduct().getId().equals(mainPresc.getProduct().getId())
                    && (mainPresc.getId() == null || (pm.getId() == null || !pm.getId().equals(mainPresc.getId())))) {
                sameProdList.add(pm);
            }
        }

        Period mainPeriod = mainPresc.getPeriod();

        for (PrescribedMedicine pm : sameProdList) {
            Period p = pm.getPeriod();

            if (p.getIniDate().compareTo(mainPeriod.getIniDate()) >= 0
                    && p.getEndDate().compareTo(mainPeriod.getEndDate()) <= 0) {
                tbcase.getPrescriptions().remove(pm);
                entityManager.remove(pm);
                break;
            }

            if (p.getIniDate().compareTo(mainPeriod.getIniDate()) < 0
                    && p.getEndDate().compareTo(mainPeriod.getEndDate()) > 0) {
                PrescribedMedicine aux = clonePrescribedMedicine(pm);
                aux.getPeriod().setIniDate(DateUtils.incDays(mainPeriod.getEndDate(), 1));
                aux.getPeriod().setEndDate(p.getEndDate());
                tbcase.getPrescriptions().add(aux);

                pm.getPeriod().setEndDate(DateUtils.incDays(mainPeriod.getIniDate(), -1));
                break;
            }

            if (p.getEndDate().compareTo(mainPeriod.getIniDate()) >= 0
                    && p.getEndDate().compareTo(mainPeriod.getEndDate()) <= 0) {
                p.setEndDate(DateUtils.incDays(mainPeriod.getIniDate(), -1));
                break;
            }

            if (p.getIniDate().compareTo(mainPeriod.getEndDate()) <= 0
                    && p.getIniDate().compareTo(mainPeriod.getIniDate()) >= 0) {
                p.setIniDate(DateUtils.incDays(mainPeriod.getEndDate(), 1));
                break;
            }
        }
    }

    /**
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

    /**
     * find the latest treatment health unit
     * @param tbcase
     * @return the latest treatment health unit
     */
    private TreatmentHealthUnit getLatestTreatHU(TbCase tbcase) {
        TreatmentHealthUnit latestTreatHU = tbcase.getTreatmentUnits().get(0);

        for (TreatmentHealthUnit thu : tbcase.getTreatmentUnits()) {
            if (latestTreatHU.getPeriod().getEndDate().before(thu.getPeriod().getEndDate())) {
                latestTreatHU = thu;
            }
        }

        return latestTreatHU;
    }

    /**
     * Check if changes in the medicine turned the regimen to an individualized
     */
    public boolean regimenMovedToIndivid(TbCase tbcase) {
        Regimen reg = tbcase.getRegimen();

        if (reg == null) {
            return true;
        }

        // get list of substances in the regimen
        List<Object> subsInRegimen = entityManager.createQuery("select s.id from Regimen r, in(r.medicines) med, in(med.medicine.substances) s " +
                "where r.id = :id and med.medicine.line <> :line")
                .setParameter("id", reg.getId())
                .setParameter("line", MedicineLine.OTHER)
                .getResultList();

        // get list of medicines prescribed
        List<Medicine> prescribedMeds = new ArrayList<Medicine>();
        for (PrescribedMedicine pm: tbcase.getPrescriptions()) {
            if (pm.getProduct() instanceof Medicine) {
                Medicine med = (Medicine) pm.getProduct();

                if ((med.getLine() != MedicineLine.OTHER) && (!prescribedMeds.contains(med))) {
                    prescribedMeds.add(med);
                }
            }
        }

        if (prescribedMeds.size() == 0) {
            return true;
        }

        // create query to select substances of the regimen
        String medCondition = null;
        for (int i = 0; i < prescribedMeds.size(); i++) {
            if (medCondition == null) {
                medCondition = " m.id = :medId" + i ;
            } else {
                medCondition = medCondition.concat(" or m.id = :medId" + i);
            }
        }

        Query query = entityManager.createQuery("select distinct s.id from Medicine m, in(m.substances) s where " + medCondition);

        for (int i = 0; i < prescribedMeds.size(); i++) {
            query.setParameter("medId" + i, prescribedMeds.get(i).getId());
        }

        List<Object> subsPrescribedId = query.getResultList();

        // check if patient is NOT TAKING any substance that is not on the Standard Regimen
        for (Object id: subsPrescribedId) {
            if (!subsInRegimen.contains(id)) {
                return true;
            }
        }

        // check if patient is TAKING any substance that is not on the Standard Regimen
        for (Object id: subsInRegimen) {
            if (!subsPrescribedId.contains(id)) {
                return true;
            }
        }

        return false;
    }
}
