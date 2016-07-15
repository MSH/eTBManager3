package org.msh.etbm.services.cases.unitview;

import org.msh.etbm.commons.Item;
import org.msh.etbm.db.enums.InfectionSite;

import java.util.Date;

/**
 * Store information about a case (TB or DR-TB)
 * Created by rmemoria on 2/6/16.
 */
public class ConfirmedCaseData extends CommonCaseData {

    private String caseNumber;
    private Item<String> registrationGroup;
    private InfectionSite infectionSite;
    private Date iniTreatmentDate;
    private Integer treatmentProgress;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public Item<String> getRegistrationGroup() {
        return registrationGroup;
    }

    public void setRegistrationGroup(Item<String> registrationGroup) {
        this.registrationGroup = registrationGroup;
    }

    public InfectionSite getInfectionSite() {
        return infectionSite;
    }

    public void setInfectionSite(InfectionSite infectionSite) {
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
