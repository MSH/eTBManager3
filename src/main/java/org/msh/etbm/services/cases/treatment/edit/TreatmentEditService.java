package org.msh.etbm.services.cases.treatment.edit;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.DateUtils;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.PrescribedMedicine;
import org.msh.etbm.db.entities.Product;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.db.entities.TreatmentHealthUnit;
import org.msh.etbm.services.cases.treatment.TreatmentCmdLogHandler;
import org.msh.etbm.services.cases.treatment.data.PrescriptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.*;

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


    /**
     * Update the treatment regimen of a case
     * @param req instance of {@link TreatmentUpdateRequest} containing the data to change
     */
    @Transactional
    @CommandLog(type = CommandTypes.CASES_TREAT_EDIT, handler = TreatmentCmdLogHandler.class)
    public void updateTreatment(TreatmentUpdateRequest req) {

    }

    /**
     * Add medicine to the treatment regimen of a case
     * @param req
     * @CommandLog(type = CommandTypes.CASES_TREAT_ADDMED, handler = TreatmentCmdLogHandler.class)
     */
    @Transactional
    public void addPrescription(AddMedicineRequest req) {
        //TODO: [MSANTOS] há mais alguma validação à fazer?
        //TODO: [MSANTOS] registrar commandlog?

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

        checkTreatPeriod(tbcase);
        entityManager.persist(tbcase);
    }

    /**
     * Update a prescription of the treatment regimen
     * @param req
     *     @CommandLog(type = CommandTypes.CASES_TREAT_PRESCEDT, handler = TreatmentCmdLogHandler.class)
     */
    @Transactional
    public void updatePrescription(PrescriptionUpdateRequest req) {
        //TODO: [MSANTOS] há mais alguma validação à fazer?
        //TODO: [MSANTOS] registrar commandlog?
        //TODO: [MSANTOS] implementar 'preserve previous period'
        //TODO: [MSANTOS] deve permitir trocar o medicamento, como na versão anterior?

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

        pm.setPeriod(p);
        pm.setDoseUnit(req.getDoseUnit());
        pm.setFrequency(req.getFrequency());
        pm.setComments(req.getComment());

        checkPrescriptionsInterceptions(pm, tbcase);

        checkTreatPeriod(tbcase);
        entityManager.persist(tbcase);
    }

    @Transactional
    public void removePrescription(UUID prescriptionId, Period p) {
        // TODO: [MSANTOS] pq desse parametro period?
        // TODO: [MSANTOS] se o prescribed medicine deletado for o que esta puxando o periodo do tratamento, deve-se reduzir o treatmentPeriod?
        // TODO: [MSANTOS] Se for a ultima prescription do caso deleta, assim mesmo?

        PrescribedMedicine pm = entityManager.find(PrescribedMedicine.class, prescriptionId);
        if (pm == null) {
            throw new EntityNotFoundException("Prescription with ID " + prescriptionId.toString() + " was not found");
        }

        TbCase tbcase = pm.getTbcase();
        tbcase.getPrescriptions().remove(pm);

        checkTreatPeriod(tbcase);
        entityManager.remove(pm);
        entityManager.persist(tbcase);
    }

    /**
     * Checks if endDate of treatment should be updated.
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
        TreatmentHealthUnit latestTreatHU = tbcase.getTreatmentUnits().get(0);

        for (TreatmentHealthUnit thu : tbcase.getTreatmentUnits()) {
            if (latestTreatHU.getPeriod().getEndDate().before(thu.getPeriod().getEndDate())) {
                latestTreatHU = thu;
            }
        }

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

    private void checkPrescriptionsInterceptions(PrescribedMedicine mainPresc, TbCase tbcase) {
        List<PrescribedMedicine> sameProdList = new ArrayList<>();

        for (PrescribedMedicine pm : tbcase.getPrescriptions()) {
            if (pm.getProduct().getId().equals(mainPresc.getProduct().getId())
                    && (mainPresc.getId() == null || !pm.getId().equals(mainPresc.getId()))) {
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
}
