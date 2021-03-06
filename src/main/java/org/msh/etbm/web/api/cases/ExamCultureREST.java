package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.EntityQueryParams;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.services.cases.followup.examculture.ExamCulData;
import org.msh.etbm.services.cases.followup.examculture.ExamCulFormData;
import org.msh.etbm.services.cases.followup.examculture.ExamCulture2Service;
import org.msh.etbm.services.cases.followup.examculture.ExamCultureService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * REST API for culture exams
 *
 * Created by msantos on 13/7/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_EXAM_CULTURE})
public class ExamCultureREST {

    @Autowired
    ExamCultureService service;

    @Autowired
    ExamCulture2Service examCulture2Service;

    @RequestMapping(value = "/examculture/init", method = RequestMethod.POST)
    public FormInitResponse init(@RequestParam(name = "id", required = false) UUID id,
                                 @RequestParam(name = "edit", required = false) String edit,
                                 @RequestParam(name = "form", required = false) String form) {
        return examCulture2Service.init(id, edit != null, form != null);
    }

    @RequestMapping(value = "/examcul/{id}", method = RequestMethod.GET)
    public ExamCulData get(@PathVariable UUID id) {
        return service.findOne(id, ExamCulData.class);
    }

    @RequestMapping(value = "/examcul", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_EXAM_CULTURE_EDT })
    public StandardResult create(@Valid @NotNull @RequestBody ExamCulFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examcul/{id}", method = RequestMethod.POST)
    @Authenticated(permissions = { Permissions.CASES_EXAM_CULTURE_EDT })
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody ExamCulFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/examcul/{id}", method = RequestMethod.DELETE)
    @Authenticated(permissions = { Permissions.CASES_EXAM_CULTURE_EDT })
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/examcul/query", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody EntityQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/examcul/form/{id}", method = RequestMethod.GET)
    public ExamCulFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, ExamCulFormData.class);
    }

}
