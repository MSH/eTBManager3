package org.msh.etbm.web.api.cases;

import org.msh.etbm.services.cases.summary.SummaryReportData;
import org.msh.etbm.services.cases.tag.CasesTagsReportItem;

import java.util.List;

/**
 * Created by rmemoria on 18/9/16.
 */
public class SummaryViewRespose {

    private List<SummaryReportData> summary;

    private List<CasesTagsReportItem> tags;

    public SummaryViewRespose(List<SummaryReportData> summary, List<CasesTagsReportItem> tags) {
        this.summary = summary;
        this.tags = tags;
    }

    public List<SummaryReportData> getSummary() {
        return summary;
    }

    public void setSummary(List<SummaryReportData> summary) {
        this.summary = summary;
    }

    public List<CasesTagsReportItem> getTags() {
        return tags;
    }

    public void setTags(List<CasesTagsReportItem> tags) {
        this.tags = tags;
    }
}
