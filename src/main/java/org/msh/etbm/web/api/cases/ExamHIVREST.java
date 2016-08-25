package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.followup.examhiv.ExamHIVData;
import org.msh.etbm.services.cases.followup.examhiv.ExamHIVFormData;
import org.msh.etbm.services.cases.followup.examhiv.ExamHIVService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 13/7/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_EXAM_CULTURE})
public class ExamHIVREST {

    @Autowired
    ExamHIVService service;

    @RequestMapping(value = "/examhiv/{id}", method = RequestMethod.GET)
    @Authenticated()
    public ExamHIVData get(@PathVariable UUID id) {
        return service.findOne(id, ExamHIVData.class);
    }

    @RequestMapping(value = "/examhiv", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody ExamHIVFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examhiv/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody ExamHIVFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examhiv/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/examhiv/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody EntityQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/examhiv/form/{id}", method = RequestMethod.GET)
    public ExamHIVFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, ExamHIVFormData.class);
    }

}
