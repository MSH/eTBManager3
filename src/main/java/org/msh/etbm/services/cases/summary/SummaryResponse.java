package org.msh.etbm.services.cases.summary;

import org.msh.etbm.services.cases.tag.CasesTagsReportItem;

import java.util.List;

/**
 * Created by rmemoria on 17/9/16.
 */
public class SummaryResponse {

    private List<SummaryReportData> summary;

    private List<CasesTagsReportItem> tags;


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
