package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.cases.prevtreats.*;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by msantos on 18/8/2016.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES_ADDINFO})
public class CasePrevTreatsREST {

    @Autowired
    CasePrevTreatService service;

    @Autowired
    FormService formService;

    @Autowired
    PrevTBTreatmentService prevTBTreatmentService;

    @RequestMapping(value = "/prevtreat", method = RequestMethod.GET)
    public FormInitResponse init(@RequestParam(name = "ro", required = false) String displaying) {
        return formService.initFromModel("prevtbtreatment", null, false);
    }

    @RequestMapping(value = "/prevtreat/{id}", method = RequestMethod.GET)
    @Authenticated()
    public CasePrevTreatData get(@PathVariable UUID id,
                                 @RequestParam(name = "ro") String readOnly,
                                 @RequestParam(name = "form") String form) {
//        formService.initFromModel("prevtbtreatment", id, readOnly != null);
        return service.findOne(id, CasePrevTreatData.class);
    }

    @RequestMapping(value = "/prevtreat", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CasePrevTreatFormData req) {
        ServiceResult res = service.create(req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/prevtreat/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody CasePrevTreatFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/prevtreat/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }

    @RequestMapping(value = "/prevtreat/query", method = RequestMethod.POST)
    @Authenticated()
    public QueryResult query(@Valid @RequestBody CasePrevTreatQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/prevtreat/form/{id}", method = RequestMethod.GET)
    public CasePrevTreatFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, CasePrevTreatFormData.class);
    }

}
