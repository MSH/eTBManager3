package org.msh.etbm.services.cases.treatment.edit;

import org.msh.etbm.commons.Messages;
import org.msh.etbm.commons.commands.CommandLog;
import org.msh.etbm.commons.commands.CommandTypes;
import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.entities.EntityValidationException;
import org.msh.etbm.db.entities.PrescribedMedicine;
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
     */
    @Transactional
    @CommandLog(type = CommandTypes.CASES_TREAT_ADDMED, handler = TreatmentCmdLogHandler.class)
    public void addMedicine(AddMedicineRequest req) {

    }

    /**
     * Update a prescription of the treatment regimen
     * @param req
     */
    @Transactional
    @CommandLog(type = CommandTypes.CASES_TREAT_PRESCEDT, handler = TreatmentCmdLogHandler.class)
    public void updatePrescription(PrescriptionUpdateRequest req) {
        PrescribedMedicine pm = entityManager.find(PrescribedMedicine.class, req.getPrescriptionId());
        if (pm == null) {
            throw new EntityNotFoundException("Prescription with ID " + req.getPrescriptionId().toString() + " was not found");
        }

        Period p = new Period(req.getIniDate(), req.getEndDate());
        if (!p.isBroken()) {
            return;
        }

        TbCase tbcase = pm.getTbcase();
        if (req.getIniDate() != null) {
            Period treatPeriod = tbcase.getTreatmentPeriod();

            if (req.getIniDate().before(treatPeriod.getIniDate())) {
                String msg = messages.format(Messages.PERIOD_INIDATE_BEFORE, req.getIniDate());
                throw new EntityValidationException(req, "iniDate", msg, null);
            }
        }


    }

    @Transactional
    public void removePrescription(UUID prescriptionId, Period p) {

    }
}
