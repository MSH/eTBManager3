package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;
import org.msh.etbm.db.enums.TreatMonitoringInput;

/**
 * Detailed data about a workspace to be sent by the service to the client
 * <p>
 * Created by rmemoria on 12/11/15.
 */
public class WorkspaceDetailData extends WorkspaceData {

    private Integer weekFreq1;
    private Integer weekFreq2;
    private Integer weekFreq3;
    private Integer weekFreq4;
    private Integer weekFreq5;
    private Integer weekFreq6;
    private Integer weekFreq7;

    /**
     * Setup the patient name composition to be used in data entry forms and displaying
     */
    private NameComposition patientNameComposition;

    /**
     * Configuration of the case validation for TB cases
     */
    private CaseValidationOption caseValidationTB;

    /**
     * Configuration of the case validation for DR-TB cases
     */
    private CaseValidationOption caseValidationDRTB;

    /**
     * Configuration of the case validation for MNT cases
     */
    private CaseValidationOption caseValidationNTM;

    /**
     * Setup the case number to be displayed for the suspect cases
     */
    private DisplayCaseNumber suspectCaseNumber;

    /**
     * Setup the case number to be displayed for the confirmed cases
     */
    private DisplayCaseNumber confirmedCaseNumber;

    /**
     * Required levels of administrative unit for patient address
     */
    private Integer patientAddrRequiredLevels;

    /**
     * Indicate if system will send e-mail messages to the users in certain system events (like new orders, orders authorized, etc)
     */
    private boolean sendSystemMessages;

    /**
     * Setup the quantity of months that the system will consider when it has to alert the user about medicines that will expire.
     */
    private Integer monthsToAlertExpiredMedicines;

    /**
     * Minimum stock on-hand in months allowed in the unit. If a medicine is equals or under this level,
     * the system will alert about that
     */
    private Integer minStockOnHand;

    /**
     * Maximum stock on-hand in months allowed in the unit. If a medicine is equals or over this level,
     * the system will alert about that
     */
    private Integer maxStockOnHand;

    /**
     * If true, in the medicine in-take monitoring of the case, user will specify if administered the treatment
     * in DOTS or if it was self-administered by the patient. If false, the user will just select the day patient
     * received medicine
     */
    private TreatMonitoringInput treatMonitoringInput;


    public Integer getWeekFreq1() {
        return weekFreq1;
    }

    public void setWeekFreq1(Integer weekFreq1) {
        this.weekFreq1 = weekFreq1;
    }

    public Integer getWeekFreq2() {
        return weekFreq2;
    }

    public void setWeekFreq2(Integer weekFreq2) {
        this.weekFreq2 = weekFreq2;
    }

    public Integer getWeekFreq3() {
        return weekFreq3;
    }

    public void setWeekFreq3(Integer weekFreq3) {
        this.weekFreq3 = weekFreq3;
    }

    public Integer getWeekFreq4() {
        return weekFreq4;
    }

    public void setWeekFreq4(Integer weekFreq4) {
        this.weekFreq4 = weekFreq4;
    }

    public Integer getWeekFreq5() {
        return weekFreq5;
    }

    public void setWeekFreq5(Integer weekFreq5) {
        this.weekFreq5 = weekFreq5;
    }

    public Integer getWeekFreq6() {
        return weekFreq6;
    }

    public void setWeekFreq6(Integer weekFreq6) {
        this.weekFreq6 = weekFreq6;
    }

    public Integer getWeekFreq7() {
        return weekFreq7;
    }

    public void setWeekFreq7(Integer weekFreq7) {
        this.weekFreq7 = weekFreq7;
    }

    public NameComposition getPatientNameComposition() {
        return patientNameComposition;
    }

    public void setPatientNameComposition(NameComposition patientNameComposition) {
        this.patientNameComposition = patientNameComposition;
    }

    public CaseValidationOption getCaseValidationTB() {
        return caseValidationTB;
    }

    public void setCaseValidationTB(CaseValidationOption caseValidationTB) {
        this.caseValidationTB = caseValidationTB;
    }

    public CaseValidationOption getCaseValidationDRTB() {
        return caseValidationDRTB;
    }

    public void setCaseValidationDRTB(CaseValidationOption caseValidationDRTB) {
        this.caseValidationDRTB = caseValidationDRTB;
    }

    public CaseValidationOption getCaseValidationNTM() {
        return caseValidationNTM;
    }

    public void setCaseValidationNTM(CaseValidationOption caseValidationNTM) {
        this.caseValidationNTM = caseValidationNTM;
    }

    public DisplayCaseNumber getSuspectCaseNumber() {
        return suspectCaseNumber;
    }

    public void setSuspectCaseNumber(DisplayCaseNumber suspectCaseNumber) {
        this.suspectCaseNumber = suspectCaseNumber;
    }

    public DisplayCaseNumber getConfirmedCaseNumber() {
        return confirmedCaseNumber;
    }

    public void setConfirmedCaseNumber(DisplayCaseNumber confirmedCaseNumber) {
        this.confirmedCaseNumber = confirmedCaseNumber;
    }

    public Integer getPatientAddrRequiredLevels() {
        return patientAddrRequiredLevels;
    }

    public void setPatientAddrRequiredLevels(Integer patientAddrRequiredLevels) {
        this.patientAddrRequiredLevels = patientAddrRequiredLevels;
    }

    public boolean isSendSystemMessages() {
        return sendSystemMessages;
    }

    public void setSendSystemMessages(boolean sendSystemMessages) {
        this.sendSystemMessages = sendSystemMessages;
    }

    public Integer getMonthsToAlertExpiredMedicines() {
        return monthsToAlertExpiredMedicines;
    }

    public void setMonthsToAlertExpiredMedicines(Integer monthsToAlertExpiredMedicines) {
        this.monthsToAlertExpiredMedicines = monthsToAlertExpiredMedicines;
    }

    public Integer getMinStockOnHand() {
        return minStockOnHand;
    }

    public void setMinStockOnHand(Integer minStockOnHand) {
        this.minStockOnHand = minStockOnHand;
    }

    public Integer getMaxStockOnHand() {
        return maxStockOnHand;
    }

    public void setMaxStockOnHand(Integer maxStockOnHand) {
        this.maxStockOnHand = maxStockOnHand;
    }

    public TreatMonitoringInput getTreatMonitoringInput() {
        return treatMonitoringInput;
    }

    public void setTreatMonitoringInput(TreatMonitoringInput treatMonitoringInput) {
        this.treatMonitoringInput = treatMonitoringInput;
    }
}
