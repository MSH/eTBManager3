package org.msh.etbm.services.cases.treatment.edit;

import org.msh.etbm.commons.InvalidArgumentException;
import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.PrescribedMedicine;
import org.msh.etbm.db.entities.Product;
import org.msh.etbm.db.entities.TbCase;
import org.msh.etbm.services.cases.treatment.TreatmentCmdLogHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
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
    public void addMedicine(AddMedicineRequest req) {
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

        entityManager.persist(pm);
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
            throw new InvalidArgumentException("Missing information on prescription period.");
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

        entityManager.persist(pm);
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

        entityManager.remove(pm);
    }
}
