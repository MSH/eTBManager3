package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.followup.examdst.ExamDSTData;
import org.msh.etbm.services.cases.followup.examdst.ExamDSTFormData;
import org.msh.etbm.services.cases.followup.examdst.ExamDSTService;
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
@Authenticated(permissions = {Permissions.CASES_EXAM_DST})
public class ExamDSTREST {

    @Autowired
    ExamDSTService service;

    @RequestMapping(value = "/examdst/{id}", method = RequestMethod.GET)
    @Authenticated()
    public ExamDSTData get(@PathVariable UUID id) {
        return service.findOne(id, ExamDSTData.class);
    }

    @RequestMapping(value = "/examdst", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody ExamDSTFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examdst/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody ExamDSTFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examdst/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/examdst/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody EntityQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/examdst/form/{id}", method = RequestMethod.GET)
    public ExamDSTFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, ExamDSTFormData.class);
    }

}
