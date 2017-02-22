package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.msh.etbm.services.cases.treatment.edit.AddMedicineRequest;
import org.msh.etbm.services.cases.treatment.edit.PrescriptionUpdateRequest;
import org.msh.etbm.services.cases.treatment.edit.TreatmentEditService;
import org.msh.etbm.services.cases.treatment.edit.TreatmentUpdateRequest;
import org.msh.etbm.services.cases.treatment.followup.TreatFollowupUpdateRequest;
import org.msh.etbm.services.cases.treatment.followup.TreatmentFollowupService;
import org.msh.etbm.services.cases.treatment.start.StartTreatmentRequest;
import org.msh.etbm.services.cases.treatment.start.StartTreatmentService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST API to expose case treatment functionalities
 *
 * Created by rmemoria on 23/5/16.
 */
@RestController
@RequestMapping("/api/cases/case")
@Authenticated(permissions = {Permissions.CASES})
public class TreatmentREST {

    @Autowired
    TreatmentFollowupService treatmentFollowupService;

    @Autowired
    StartTreatmentService startTreatmentService;

    @Autowired
    TreatmentService treatmentService;

    @Autowired
    TreatmentEditService treatmentEditService;

    /**
     * Return treatment information of a case
     * @param caseId the ID of the case to get information from
     * @return
     */
    @RequestMapping(value = "/treatment/{caseId}", method = RequestMethod.GET)
    @Authenticated
    public TreatmentData get(@PathVariable @NotNull UUID caseId) {
        return treatmentService.getData(caseId);
    }

    /**
     * Update the treatment followup of a case for a given month/year
     * @param req the client request with information about the month of the treatment to update
     * @return instance of {@link StandardResult}
     */
    @RequestMapping(value = "/treatment/followup", method = RequestMethod.POST)
    public StandardResult update(@RequestBody @Valid @NotNull TreatFollowupUpdateRequest req) {
        treatmentFollowupService.updateTreatmentFollowup(req);

        return StandardResult.createSuccessResult();
    }

    /**
     * Start a treatment of a case using a standard regimen
     * @param req instance of {@link StartTreatmentRequest} containing the treatment data
     * @return instance of {@link StandardResult}
     */
    @RequestMapping(value = "/treatment/start", method = RequestMethod.POST)
    public StandardResult startStandardRegimen(@RequestBody @Valid @NotNull StartTreatmentRequest req) {
        startTreatmentService.startTreatment(req);

        return StandardResult.createSuccessResult();
    }

    /**
     * Undo a treatment for a case
     * @param caseId
     * @return
     */
    @RequestMapping(value = "/treatment/undo/{caseId}", method = RequestMethod.POST)
    public StandardResult undoTreatment(@PathVariable @NotNull UUID caseId) {
        treatmentService.undoTreatment(caseId);

        return StandardResult.createSuccessResult();
    }

    /**
     * remove a prescription from the case treatment
     * @param prescriptionId
     * @return
     */
    @RequestMapping(value = "/treatment/prescription/delete/{prescriptionId}", method = RequestMethod.DELETE)
    public StandardResult deletePrescription(@PathVariable @NotNull UUID prescriptionId) {
        treatmentEditService.removePrescription(prescriptionId);

        return StandardResult.createSuccessResult();
    }

    /**
     * creates a prescription from the case treatment
     * @param req
     * @return
     */
    @RequestMapping(value = "/treatment/prescription/add", method = RequestMethod.POST)
    public StandardResult addPrescription(@RequestBody @Valid @NotNull AddMedicineRequest req) {
        treatmentEditService.addPrescription(req);

        return StandardResult.createSuccessResult();
    }

    /**
     * updates a prescription from the case treatment
     * @param req
     * @return
     */
    @RequestMapping(value = "/treatment/prescription/update", method = RequestMethod.POST)
    public StandardResult updatePrescription(@RequestBody @Valid @NotNull PrescriptionUpdateRequest req) {
        treatmentEditService.updatePrescription(req);

        return StandardResult.createSuccessResult();
    }

    /**
     * updates the treatment end date of the case
     * @param req
     * @return
     */
    @RequestMapping(value = "/treatment/update", method = RequestMethod.POST)
    public StandardResult updateTreatment(@RequestBody @Valid @NotNull TreatmentUpdateRequest req) {
        treatmentEditService.updateTreatment(req);

        return StandardResult.createSuccessResult();
    }
}
