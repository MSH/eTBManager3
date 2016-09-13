package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.msh.etbm.services.cases.treatment.followup.TreatFollowupUpdateRequest;
import org.msh.etbm.services.cases.treatment.followup.TreatmentFollowupService;
import org.msh.etbm.services.cases.treatment.start.StartIndividualizedRegimenRequest;
import org.msh.etbm.services.cases.treatment.start.StartStandardRegimenRequest;
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
    TreatmentService service;

    @Autowired
    TreatmentFollowupService treatmentFollowupService;

    @Autowired
    StartTreatmentService startTreatmentService;

    @Autowired
    TreatmentService treatmentService;


    /**
     * Return treatment information of a case
     * @param caseId the ID of the case to get information from
     * @return
     */
    @RequestMapping(value = "/treatment/{caseId}", method = RequestMethod.GET)
    @Authenticated
    public TreatmentData get(@PathVariable @NotNull UUID caseId) {
        return service.getData(caseId);
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
     * @param req instance of {@link StartStandardRegimenRequest} containing the treatment data
     * @return instance of {@link StandardResult}
     */
    @RequestMapping(value = "/treatment/start/standard", method = RequestMethod.POST)
    public StandardResult startStandardRegimen(@RequestBody @Valid @NotNull StartStandardRegimenRequest req) {
        startTreatmentService.startStandardRegimen(req);

        return StandardResult.createSuccessResult();
    }

    /**
     * Start a treatment of a case using an individualized regimen
     * @param req instance of {@link StartIndividualizedRegimenRequest} containing the treatment data
     * @return instance of {@link StandardResult}
     */
    @RequestMapping(value = "/treatment/start/indiv", method = RequestMethod.POST)
    public StandardResult startStandardRegimen(@RequestBody @Valid @NotNull StartIndividualizedRegimenRequest req) {
        startTreatmentService.startInividualizedRegimen(req);

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
}
