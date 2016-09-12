package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.ServiceResult;
import org.msh.etbm.commons.forms.FormInitResponse;
import org.msh.etbm.commons.forms.FormService;
import org.msh.etbm.services.cases.cases.*;
import org.msh.etbm.services.cases.comorbidity.ComorbidityFormData;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by msantos on 26/3/16.
 */
@RestController
@RequestMapping("/api/tbl")
@Authenticated(permissions = {Permissions.CASES})
public class CasesREST {

    @Autowired
    CaseService service;

    @Autowired
    NewNotificationService newNotificationService;

    @Autowired
    FormService formService;

    @RequestMapping(value = "/case/{id}", method = RequestMethod.GET)
    public CaseDetailedData get(@PathVariable UUID id) {
        return service.findOne(id, CaseDetailedData.class);
    }

    /*
    @RequestMapping(value = "/case/{id}", method = RequestMethod.POST)
    public StandardResult update(@PathVariable UUID id, @Valid @NotNull @RequestBody ComorbidityFormData req) {
        ServiceResult res = service.update(id, req);
        return new StandardResult(res);
    }

    @RequestMapping(value = "/case/query", method = RequestMethod.POST)
    public QueryResult query(@Valid @RequestBody CaseQueryParams query) {
        return service.findMany(query);
    }

    @RequestMapping(value = "/case/form/{id}", method = RequestMethod.GET)
    public ComorbidityFormData getForm(@PathVariable UUID id) {
        return service.findOne(id, ComorbidityFormData.class);
    }
    */

    @RequestMapping(value = "/case/initform")
    public FormInitResponse initForm(@Valid @NotNull @RequestBody CaseInitFormReq req) {

        // mount doc
        Map<String, Object> doc = new HashMap<>();
        doc.put("tbcase", new HashMap<>());
        doc.put("patient", new HashMap<>());

        // generate form id
        String formid = "newnotif-";
        formid = formid.concat(req.getDiagnosisType().name().toLowerCase()).concat("-");
        formid = formid.concat(req.getCaseClassification().name().toLowerCase());

        return formService.init(formid, doc, false);
    }

    @RequestMapping(value = "/case", method = RequestMethod.POST)
    public StandardResult create(@Valid @NotNull @RequestBody CaseFormData req) {
        return newNotificationService.create(req);
    }

    @RequestMapping(value = "/case/{id}", method = RequestMethod.DELETE)
    public StandardResult delete(@PathVariable @NotNull UUID id) {
        service.delete(id).getId();
        return new StandardResult(id, null, true);
    }
}
