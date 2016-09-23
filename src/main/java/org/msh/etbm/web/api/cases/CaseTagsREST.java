package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.tag.CaseTagsFormData;
import org.msh.etbm.services.cases.tag.ManualCaseTagsService;
import org.msh.etbm.services.cases.tag.TagCasesQueryParams;
import org.msh.etbm.services.cases.tag.TagCasesQueryService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.StandardResult;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Mauricio on 25/07/2016.
 */
@RestController
@RequestMapping("/api/cases/tag")
@Authenticated(permissions = {Permissions.CASES_TAG})
public class CaseTagsREST {

    @Autowired
    ManualCaseTagsService service;

    @Autowired
    TagCasesQueryService tagCasesQueryService;

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public StandardResult updateManualTags(@Valid @NotNull @RequestBody CaseTagsFormData req) {
        service.updateTags(req);
        return StandardResult.createSuccessResult();
    }

    @RequestMapping(value = "/query", method = RequestMethod.POST)
    public QueryResult getTagCases(@Valid @RequestBody TagCasesQueryParams query) {
        return tagCasesQueryService.getTagCases(query);
    }

}
