package org.msh.etbm.web.api.cases;

import org.msh.etbm.commons.entities.query.QueryResult;
import org.msh.etbm.services.cases.tag.TagCasesReportService;
import org.msh.etbm.services.cases.tag.TagCasesQueryParams;
import org.msh.etbm.services.cases.unitview.UnitViewData;
import org.msh.etbm.services.cases.unitview.UnitViewService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

/**
 * REST api to provide data of a unit view in the case management module
 * <p>
 * Created by rmemoria on 3/6/16.
 */
@RestController
@RequestMapping("/api/cases")
@Authenticated(permissions = {Permissions.CASES})
public class UnitViewREST {

    @Autowired
    UnitViewService service;

    @Autowired
    TagCasesReportService tagCasesService;

    @RequestMapping(value = "/unit/{unitID}", method = RequestMethod.POST)
    public UnitViewData getUnitView(@PathVariable UUID unitID) {
        return service.getUnitView(unitID);
    }

    @RequestMapping(value = "/unit/tag/", method = RequestMethod.POST)
    public QueryResult getTagCases(@Valid @RequestBody TagCasesQueryParams query) {
        return tagCasesService.getTagCasesByUnit(query);
    }
}
