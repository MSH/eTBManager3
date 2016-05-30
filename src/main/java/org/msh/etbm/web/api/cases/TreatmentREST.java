package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.treatment.data.TreatmentData;
import org.msh.etbm.services.cases.treatment.TreatmentService;
import org.msh.etbm.services.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by rmemoria on 23/5/16.
 */
@RestController
@RequestMapping("/api/cases")
@Authenticated(permissions = {Permissions.CASES})
public class TreatmentREST {

    @Autowired
    TreatmentService service;

    @RequestMapping(value = "/treatment/{caseId}", method = RequestMethod.GET)
    @Authenticated
    public TreatmentData get(@PathVariable UUID caseId) {
        return service.get(caseId);
    }

}
