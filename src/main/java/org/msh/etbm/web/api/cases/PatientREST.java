package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.patient.PatientQueryParams;
import org.msh.etbm.services.cases.patient.PatientSearchItem;
import org.msh.etbm.services.cases.patient.PatientService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by msantos on 01/09/2016.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES})
public class PatientREST {

    @Autowired
    PatientService service;

    @RequestMapping(value = "/patient/search", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult<PatientSearchItem> query(@Valid @RequestBody PatientQueryParams query) {
        return service.searchPatient(query);
    }

}
