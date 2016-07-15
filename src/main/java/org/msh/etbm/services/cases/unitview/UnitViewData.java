package org.msh.etbm.services.cases.unitview;

import org.msh.etbm.services.admin.tags.CasesTagsReportItem;
import org.msh.etbm.services.admin.units.data.UnitData;

import java.util.List;

/**
 * Created by rmemoria on 2/6/16.
 */
public class UnitViewData {

    private UnitData unit;
    private List<PresumptiveCaseData> presumptives;
    private List<ConfirmedCaseData> tbCases;
    private List<ConfirmedCaseData> drtbCases;
    private List<ConfirmedCaseData> ntmCases;
    private List<CasesTagsReportItem> tags;

    public UnitData getUnit() {
        return unit;
    }

    public void setUnit(UnitData unit) {
        this.unit = unit;
    }

    public List<PresumptiveCaseData> getPresumptives() {
        return presumptives;
    }

    public void setPresumptives(List<PresumptiveCaseData> presumptives) {
        this.presumptives = presumptives;
    }

    public List<ConfirmedCaseData> getTbCases() {
        return tbCases;
    }

    public void setTbCases(List<ConfirmedCaseData> tbCases) {
        this.tbCases = tbCases;
    }

    public List<ConfirmedCaseData> getDrtbCases() {
        return drtbCases;
    }

    public void setDrtbCases(List<ConfirmedCaseData> drtbCases) {
        this.drtbCases = drtbCases;
    }

    public List<CasesTagsReportItem> getTags() {
        return tags;
    }

    public void setTags(List<CasesTagsReportItem> tags) {
        this.tags = tags;
    }

    public List<ConfirmedCaseData> getNtmCases() {
        return ntmCases;
    }

    public void setNtmCases(List<ConfirmedCaseData> ntmCases) {
        this.ntmCases = ntmCases;
    }
}
