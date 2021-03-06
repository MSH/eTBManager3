package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.followup.examxpert.ExamXpertData;
import org.msh.etbm.services.cases.followup.examxpert.ExamXpertFormData;
import org.msh.etbm.services.cases.followup.examxpert.ExamXpertService;
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
@Authenticated(permissions = { Permissions.CASES_EXAM_XPERT })
public class ExamXpertREST {

    @Autowired
    ExamXpertService service;

    @RequestMapping(value = "/examxpert/{id}", method = RequestMethod.GET)
    public ExamXpertData get(@PathVariable UUID id) {
        return service.findOne(id, ExamXpertData.class);
    }

    @RequestMapping(value = "/examxpert", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_EXAM_XPERT_EDT })
    public StandardResult create(@Valid @NotNull @RequestBody ExamXpertFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examxpert/{id}", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_EXAM_XPERT_EDT })
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody ExamXpertFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examxpert/{id}", method = RequestMethod.DELETE)
    @Authenticated(permissions = { Permissions.CASES_EXAM_XPERT_EDT })
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/examxpert/query", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody EntityQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/examxpert/form/{id}", method = RequestMethod.GET)
    public ExamXpertFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, ExamXpertFormData.class);
    }

}
