package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.followup.examxray.ExamXRayData;
import org.msh.etbm.services.cases.followup.examxray.ExamXRayFormData;
import org.msh.etbm.services.cases.followup.examxray.ExamXRayService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 14/7/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_EXAM_CULTURE})
public class ExamXRayREST {

    @Autowired
    ExamXRayService service;

    @RequestMapping(value = "/examxray/{id}", method = RequestMethod.GET)
    @Authenticated()
    public ExamXRayData get(@PathVariable UUID id) {
        return service.findOne(id, ExamXRayData.class);
    }

    @RequestMapping(value = "/examxray", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody ExamXRayFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examxray/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody ExamXRayFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examxray/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/examxray/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody EntityQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/examxray/form/{id}", method = RequestMethod.GET)
    public ExamXRayFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, ExamXRayFormData.class);
    }

}
