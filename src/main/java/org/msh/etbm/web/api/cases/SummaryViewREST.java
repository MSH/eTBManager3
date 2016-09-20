package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.summary.SummaryReportData;
import org.msh.etbm.services.cases.summary.SummaryRequest;
import org.msh.etbm.services.cases.summary.SummaryService;
import org.msh.etbm.services.cases.tag.CasesTagsReportItem;
import org.msh.etbm.services.cases.tag.TagsReportRequest;
import org.msh.etbm.services.cases.tag.TagsReportService;
import org.msh.etbm.services.cases.view.unitview.UnitViewData;
import org.msh.etbm.services.cases.view.unitview.UnitViewService;
import org.msh.etbm.services.security.permissions.Permissions;
import org.msh.etbm.web.api.authentication.Authenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * REST api to provide data of a unit view in the case management module
 * <p>
 * Created by rmemoria on 3/6/16.
 */
@RestController
@RequestMapping("/api/cases")
@Authenticated(permissions = {Permissions.CASES})
public class SummaryViewREST {

    @Autowired
    UnitViewService service;

    @Autowired
    SummaryService summaryService;

    @Autowired
    TagsReportService tagsReportService;


    @RequestMapping(value = "/unit/{unitID}", method = RequestMethod.POST)
    public UnitViewData getUnitView(@PathVariable UUID unitID) {
        return service.getUnitView(unitID);
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public SummaryViewRespose getSummary(@RequestBody @Valid @NotNull SummaryRequest req) {
        // get the summary report
        List<SummaryReportData> summary = summaryService.generateSummary(req);

        // get the tags report
        List<CasesTagsReportItem> tags = tagsReportService.generate(
                new TagsReportRequest(req.getScope(), req.getScopeId()));

        return new SummaryViewRespose(summary, tags);
    }
}
