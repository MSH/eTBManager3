package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.followup.medexam.MedExamData;
import org.msh.etbm.services.cases.followup.medexam.MedExamFormData;
import org.msh.etbm.services.cases.followup.medexam.MedExamService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 11/7/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = { Permissions.CASES_MED_EXAM })
public class MedicalExaminationREST {

    @Autowired
    MedExamService service;

    @RequestMapping(value = "/medexam/{id}", method = RequestMethod.GET)
    public MedExamData get(@PathVariable UUID id) {
        return service.findOne(id, MedExamData.class);
    }

    @RequestMapping(value = "/medexam", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_MED_EXAM_EDT })
    public StandardResult create(@Valid @NotNull @RequestBody MedExamFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/medexam/{id}", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_MED_EXAM_EDT })
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody MedExamFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/medexam/{id}", method = RequestMethod.DELETE)
    @Authenticated(permissions = { Permissions.CASES_MED_EXAM_EDT })
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/medexam/query", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody EntityQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/medexam/form/{id}", method = RequestMethod.GET)
    public MedExamFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, MedExamFormData.class);
    }

}
