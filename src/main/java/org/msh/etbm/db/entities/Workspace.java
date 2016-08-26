package org.msh.etbm.db.entities;

import org.msh.etbm.commons.Displayable;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.Synchronizable;
import org.msh.etbm.db.enums.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workspace")
public class Workspace extends Synchronizable implements Displayable {

    @PropertyLog(messageKey = "form.name")
    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @PropertyLog(ignore = true)
    private List<UserWorkspace> users = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    @PropertyLog(ignore = true)
    private WorkspaceView view;

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

    /**
     * If true, in the medicine in-take monitoring of the case, user will specify if administered the treatment
     * in DOTS or if it was self-administered by the patient. If false, the user will just select the day patient
     * received medicine
     */
    private TreatMonitoringInput treatMonitoringInput;

    @PropertyLog(messageKey = "form.customId")
    private String customId;


    /**
     * Return the case validation setting of the given case classification
     *
     * @param classification is the classification of the case
     * @return instance of {@link CaseValidationOption}
     */
    public CaseValidationOption getCaseValidationOption(CaseClassification classification) {
        switch (classification) {
            case TB:
                return caseValidationTB;
            case DRTB:
                return caseValidationDRTB;
            case NTM:
                return caseValidationNTM;
        }
        return null;
    }


    @Override
    public String toString() {
        return "(" + getId() + ") " + getName().toString();
    }


    /**
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public List<UserWorkspace> getUsers() {
        return users;
    }

    public void setUsers(List<UserWorkspace> users) {
        this.users = users;
    }

    /**
     * @return the patientNameComposition
     */
    public NameComposition getPatientNameComposition() {
        return patientNameComposition;
    }

    /**
     * @param patientNameComposition the patientNameComposition to set
     */
    public void setPatientNameComposition(NameComposition patientNameComposition) {
        this.patientNameComposition = patientNameComposition;
    }

    /**
     * @return the view
     */
    public WorkspaceView getView() {
        return view;
    }

    /**
     * @param view the view to set
     */
    public void setView(WorkspaceView view) {
        this.view = view;
    }

    /**
     * @return the sendSystemMessages
     */
    public boolean isSendSystemMessages() {
        return sendSystemMessages;
    }

    /**
     * @param sendSystemMessages the sendSystemMessages to set
     */
    public void setSendSystemMessages(boolean sendSystemMessages) {
        this.sendSystemMessages = sendSystemMessages;
    }

    /**
     * @return the monthsToAlertExpiredMedicines
     */
    public Integer getMonthsToAlertExpiredMedicines() {
        return monthsToAlertExpiredMedicines;
    }

    /**
     * @param monthsToAlertExpiredMedicines the monthsToAlertExpiredMedicines to set
     */
    public void setMonthsToAlertExpiredMedicines(
            Integer monthsToAlertExpiredMedicines) {
        this.monthsToAlertExpiredMedicines = monthsToAlertExpiredMedicines;
    }


    /**
     * @return the caseValidationTB
     */
    public CaseValidationOption getCaseValidationTB() {
        return caseValidationTB;
    }

    /**
     * @param caseValidationTB the caseValidationTB to set
     */
    public void setCaseValidationTB(CaseValidationOption caseValidationTB) {
        this.caseValidationTB = caseValidationTB;
    }

    /**
     * @return the caseValidationDRTB
     */
    public CaseValidationOption getCaseValidationDRTB() {
        return caseValidationDRTB;
    }

    /**
     * @param caseValidationDRTB the caseValidationDRTB to set
     */
    public void setCaseValidationDRTB(CaseValidationOption caseValidationDRTB) {
        this.caseValidationDRTB = caseValidationDRTB;
    }

    /**
     * @return the caseValidationNTM
     */
    public CaseValidationOption getCaseValidationNTM() {
        return caseValidationNTM;
    }

    /**
     * @param caseValidationNTM the caseValidationNTM to set
     */
    public void setCaseValidationNTM(CaseValidationOption caseValidationNTM) {
        this.caseValidationNTM = caseValidationNTM;
    }

    /**
     * @return the minStockOnHand
     */
    public Integer getMinStockOnHand() {
        return minStockOnHand;
    }

    /**
     * @param minStockOnHand the minStockOnHand to set
     */
    public void setMinStockOnHand(Integer minStockOnHand) {
        this.minStockOnHand = minStockOnHand;
    }

    /**maxStockOnHand
     * @return the
     */
    public Integer getMaxStockOnHand() {
        return maxStockOnHand;
    }

    /**
     * @param maxStockOnHand the maxStockOnHand to set
     */
    public void setMaxStockOnHand(Integer maxStockOnHand) {
        this.maxStockOnHand = maxStockOnHand;
    }

    /**
     * @return the suspectCaseNumber
     */
    public DisplayCaseNumber getSuspectCaseNumber() {
        return suspectCaseNumber;
    }

    /**
     * @param suspectCaseNumber the suspectCaseNumber to set
     */
    public void setSuspectCaseNumber(DisplayCaseNumber suspectCaseNumber) {
        this.suspectCaseNumber = suspectCaseNumber;
    }

    /**
     * @return the confirmedCaseNumber
     */
    public DisplayCaseNumber getConfirmedCaseNumber() {
        return confirmedCaseNumber;
    }

    /**
     * @param confirmedCaseNumber the confirmedCaseNumber to set
     */
    public void setConfirmedCaseNumber(DisplayCaseNumber confirmedCaseNumber) {
        this.confirmedCaseNumber = confirmedCaseNumber;
    }

    /**
     * @return the treatMonitoringInput
     */
    public TreatMonitoringInput getTreatMonitoringInput() {
        return treatMonitoringInput;
    }

    /**
     * @param treatMonitoringInput the treatMonitoringInput to set
     */
    public void setTreatMonitoringInput(TreatMonitoringInput treatMonitoringInput) {
        this.treatMonitoringInput = treatMonitoringInput;
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }
}
