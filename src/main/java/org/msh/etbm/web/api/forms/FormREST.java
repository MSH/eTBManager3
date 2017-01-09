package org.msh.etbm.web.api.forms;

import org.msh.etbm.commons.forms.FormRequest;
import org.msh.etbm.commons.forms.FormRequestService;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * API to handle general form initialization, avoid several requests to initialize fields
 * individually
 * <p>
 * Created by rmemoria on 18/1/16.
 */
@RestController
@RequestMapping("/api/form")
@Authenticated
public class FormREST {

    @Autowired
    FormRequestService formRequestService;

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public Map<String, Object> initFields(@Valid @NotNull @RequestBody List<FormRequest> req) {
        return formRequestService.processFormRequests(req);
    }
}
