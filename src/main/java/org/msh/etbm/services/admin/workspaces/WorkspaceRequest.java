package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.db.enums.TreatMonitoringInput;

import java.util.Optional;

/**
 * Information to be sent to the service component to create or update a workspace
 *
 * Created by rmemoria on 12/11/15.
 */
public class WorkspaceRequest {

    public static final String PROFILE_ITEM = "item";
    public static final String PROFILE_DEFAULT = "default";
    public static final String PROFILE_DETAILED = "detailed";

    public static final String ORDERBY_NAME = "name";

    /**
     * Just used when creating a new workspace, to indicate if template data
     * (like medicines and regimens) will be included as well
     */
    private boolean addTemplateData;

    private Optional<String> name;

    private Optional<Integer> weekFreq1;
    private Optional<Integer> weekFreq2;
    private Optional<Integer> weekFreq3;
    private Optional<Integer> weekFreq4;
    private Optional<Integer> weekFreq5;
    private Optional<Integer> weekFreq6;
    private Optional<Integer> weekFreq7;

    private Optional<NameComposition> patientNameComposition;

    private Optional<CaseValidationOption> caseValidationTB;
    private Optional<CaseValidationOption> caseValidationDRTB;
    private Optional<CaseValidationOption> caseValidationNTM;

    private Optional<DisplayCaseNumber> suspectCaseNumber;
    private Optional<DisplayCaseNumber> confirmedCaseNumber;

    private Optional<Integer> patientAddrRequiredLevels;
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

    public Optional<Integer> getWeekFreq1() {
        return weekFreq1;
    }

    public void setWeekFreq1(Optional<Integer> weekFreq1) {
        this.weekFreq1 = weekFreq1;
    }

    public Optional<Integer> getWeekFreq2() {
        return weekFreq2;
    }

    public void setWeekFreq2(Optional<Integer> weekFreq2) {
        this.weekFreq2 = weekFreq2;
    }

    public Optional<Integer> getWeekFreq3() {
        return weekFreq3;
    }

    public void setWeekFreq3(Optional<Integer> weekFreq3) {
        this.weekFreq3 = weekFreq3;
    }

    public Optional<Integer> getWeekFreq4() {
        return weekFreq4;
    }

    public void setWeekFreq4(Optional<Integer> weekFreq4) {
        this.weekFreq4 = weekFreq4;
    }

    public Optional<Integer> getWeekFreq5() {
        return weekFreq5;
    }

    public void setWeekFreq5(Optional<Integer> weekFreq5) {
        this.weekFreq5 = weekFreq5;
    }

    public Optional<Integer> getWeekFreq6() {
        return weekFreq6;
    }

    public void setWeekFreq6(Optional<Integer> weekFreq6) {
        this.weekFreq6 = weekFreq6;
    }

    public Optional<Integer> getWeekFreq7() {
        return weekFreq7;
    }

    public void setWeekFreq7(Optional<Integer> weekFreq7) {
        this.weekFreq7 = weekFreq7;
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

    public Optional<Integer> getPatientAddrRequiredLevels() {
        return patientAddrRequiredLevels;
    }

    public void setPatientAddrRequiredLevels(Optional<Integer> patientAddrRequiredLevels) {
        this.patientAddrRequiredLevels = patientAddrRequiredLevels;
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
