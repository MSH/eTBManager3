package org.msh.etbm.web.api.forms;

import org.msh.etbm.commons.forms.FieldInitRequest;
import org.msh.etbm.commons.forms.FormsService;
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
 *
 * Created by rmemoria on 18/1/16.
 */
@RestController
@RequestMapping("/api/form")
@Authenticated
public class FormsREST {

    @Autowired
    FormsService formsService;

    @RequestMapping(value = "/initfields", method = RequestMethod.POST)
    public Map<String, Object> initFields(@Valid @NotNull @RequestBody List<FieldInitRequest> req) {
        return formsService.initFormFields(req);
    }
}
