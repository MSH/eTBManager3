package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.db.enums.TreatMonitoringInput;

import java.util.Optional;

/**
 * Information to be sent to the service component to create or update a workspace
 * <p>
 * Created by rmemoria on 12/11/15.
 */
public class WorkspaceRequest {

    /**
     * Just used when creating a new workspace, to indicate if template data
     * (like medicines and regimens) will be included as well
     */
    private boolean addTemplateData;

    private Optional<String> name;

    private Optional<NameComposition> patientNameComposition;

    private Optional<CaseValidationOption> caseValidationTB;
    private Optional<CaseValidationOption> caseValidationDRTB;
    private Optional<CaseValidationOption> caseValidationNTM;

    private Optional<DisplayCaseNumber> suspectCaseNumber;
    private Optional<DisplayCaseNumber> confirmedCaseNumber;

    private Optional<Boolean> sendSystemMessages;

    private Optional<Integer> monthsToAlertExpiredMedicines;
    private Optional<Integer> minStockOnHand;
    private Optional<Integer> maxStockOnHand;

    private Optional<TreatMonitoringInput> treatMonitoringInput;


    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<NameComposition> getPatientNameComposition() {
        return patientNameComposition;
    }

    public void setPatientNameComposition(Optional<NameComposition> patientNameComposition) {
        this.patientNameComposition = patientNameComposition;
    }

    public Optional<CaseValidationOption> getCaseValidationTB() {
        return caseValidationTB;
    }

    public void setCaseValidationTB(Optional<CaseValidationOption> caseValidationTB) {
        this.caseValidationTB = caseValidationTB;
    }

    public Optional<CaseValidationOption> getCaseValidationDRTB() {
        return caseValidationDRTB;
    }

    public void setCaseValidationDRTB(Optional<CaseValidationOption> caseValidationDRTB) {
        this.caseValidationDRTB = caseValidationDRTB;
    }

    public Optional<CaseValidationOption> getCaseValidationNTM() {
        return caseValidationNTM;
    }

    public void setCaseValidationNTM(Optional<CaseValidationOption> caseValidationNTM) {
        this.caseValidationNTM = caseValidationNTM;
    }

    public Optional<DisplayCaseNumber> getSuspectCaseNumber() {
        return suspectCaseNumber;
    }

    public void setSuspectCaseNumber(Optional<DisplayCaseNumber> suspectCaseNumber) {
        this.suspectCaseNumber = suspectCaseNumber;
    }

    public Optional<DisplayCaseNumber> getConfirmedCaseNumber() {
        return confirmedCaseNumber;
    }

    public void setConfirmedCaseNumber(Optional<DisplayCaseNumber> confirmedCaseNumber) {
        this.confirmedCaseNumber = confirmedCaseNumber;
    }

    public Optional<Boolean> getSendSystemMessages() {
        return sendSystemMessages;
    }

    public void setSendSystemMessages(Optional<Boolean> sendSystemMessages) {
        this.sendSystemMessages = sendSystemMessages;
    }

    public Optional<Integer> getMonthsToAlertExpiredMedicines() {
        return monthsToAlertExpiredMedicines;
    }

    public void setMonthsToAlertExpiredMedicines(Optional<Integer> monthsToAlertExpiredMedicines) {
        this.monthsToAlertExpiredMedicines = monthsToAlertExpiredMedicines;
    }

    public Optional<Integer> getMinStockOnHand() {
        return minStockOnHand;
    }

    public void setMinStockOnHand(Optional<Integer> minStockOnHand) {
        this.minStockOnHand = minStockOnHand;
    }

    public Optional<Integer> getMaxStockOnHand() {
        return maxStockOnHand;
    }

    public void setMaxStockOnHand(Optional<Integer> maxStockOnHand) {
        this.maxStockOnHand = maxStockOnHand;
    }

    public Optional<TreatMonitoringInput> getTreatMonitoringInput() {
        return treatMonitoringInput;
    }

    public void setTreatMonitoringInput(Optional<TreatMonitoringInput> treatMonitoringInput) {
        this.treatMonitoringInput = treatMonitoringInput;
    }

    public boolean isAddTemplateData() {
        return addTemplateData;
    }

    public void setAddTemplateData(boolean addTemplateData) {
        this.addTemplateData = addTemplateData;
    }
}
