package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.msh.etbm.services.cases.treatment.followup.TreatFollowupUpdateRequest;
import org.msh.etbm.services.cases.treatment.followup.TreatmentFollowupService;
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

    /**
     * Return treatment information of a case
     * @param caseId the ID of the case to get information from
     * @return
     */
    @RequestMapping(value = "/treatment/{caseId}", method = RequestMethod.GET)
    @Authenticated
    public TreatmentData get(@PathVariable UUID caseId) {
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
}
