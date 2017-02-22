package org.msh.etbm.services.admin.workspaces;

import org.msh.etbm.db.enums.CaseValidationOption;
import org.msh.etbm.db.enums.DisplayCaseNumber;
import org.msh.etbm.db.enums.NameComposition;

/**
 * Detailed data about a workspace to be sent by the service to the client
 * <p>
 * Created by rmemoria on 12/11/15.
 */
public class WorkspaceDetailData extends WorkspaceData {

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
}
