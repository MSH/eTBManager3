package org.msh.etbm.services.cases.view.unitview;

import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.InfectionSite;

import java.util.Date;

/**
 * Store information about a case (TB or DR-TB)
 * Created by rmemoria on 2/6/16.
 */
public class ConfirmedCaseData extends CommonCaseData {

    private String caseNumber;
    private String registrationGroup;
    private Item<InfectionSite> infectionSite;
    private Date iniTreatmentDate;
    private Integer treatmentProgress;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getRegistrationGroup() {
        return registrationGroup;
    }

    public void setRegistrationGroup(String registrationGroup) {
        this.registrationGroup = registrationGroup;
    }

    public Item<InfectionSite> getInfectionSite() {
        return infectionSite;
    }

    public void setInfectionSite(Item<InfectionSite> infectionSite) {
        this.infectionSite = infectionSite;
    }

    public Date getIniTreatmentDate() {
        return iniTreatmentDate;
    }

    public void setIniTreatmentDate(Date iniTreatmentDate) {
        this.iniTreatmentDate = iniTreatmentDate;
    }

    public Integer getTreatmentProgress() {
        return treatmentProgress;
    }

    public void setTreatmentProgress(Integer treatmentProgress) {
        this.treatmentProgress = treatmentProgress;
    }
}
