package org.msh.etbm.services.init.demodata.data;

import org.msh.etbm.services.cases.cases.data.CaseDetailedData;

import java.util.List;

/**
 * Stores data about tbcases created as demonstration data
 * Created by Mauricio on 19/10/2016.
 */
public class CaseDemoData extends CaseDetailedData {

    /**
     * custom regimen id so the regimen will be related to this case
     */
    private String customRegimenId;

    /**
     * notification address admin unit name, so the admin unit will be found by this condition
     * and related to the case
     */
    private String notifAddrAdminUnitName;

    /**
     * current address admin unit name, so the admin unit will be found by this condition
     * and related to the case
     */
    private String currAddrAdminUnitName;

    /**
     * owner unit name, so the unit will be found by this condition
     * and related to the case
     */
    private String ownerUnitName;

    /**
     * notification unit name, so the unit will be found by this condition
     * and related to the case
     */
    private String notifUnitName;

    private List<PrescriptionDemoData> prescriptionDemoData;

    private List<TreatmentUnitDemoData> treatmentUnitDemoDatas;

    public List<TreatmentUnitDemoData> getTreatmentUnitDemoDatas() {
        return treatmentUnitDemoDatas;
    }

    public void setTreatmentUnitDemoDatas(List<TreatmentUnitDemoData> treatmentUnitDemoDatas) {
        this.treatmentUnitDemoDatas = treatmentUnitDemoDatas;
    }

    public List<PrescriptionDemoData> getPrescriptionDemoData() {
        return prescriptionDemoData;
    }

    public void setPrescriptionDemoData(List<PrescriptionDemoData> prescriptionDemoData) {
        this.prescriptionDemoData = prescriptionDemoData;
    }

    public String getCustomRegimenId() {
        return customRegimenId;
    }

    public void setCustomRegimenId(String customRegimenId) {
        this.customRegimenId = customRegimenId;
    }

    public String getNotifAddrAdminUnitName() {
        return notifAddrAdminUnitName;
    }

    public void setNotifAddrAdminUnitName(String notifAddrAdminUnitName) {
        this.notifAddrAdminUnitName = notifAddrAdminUnitName;
    }

    public String getCurrAddrAdminUnitName() {
        return currAddrAdminUnitName;
    }

    public void setCurrAddrAdminUnitName(String currAddrAdminUnitName) {
        this.currAddrAdminUnitName = currAddrAdminUnitName;
    }

    public String getOwnerUnitName() {
        return ownerUnitName;
    }

    public void setOwnerUnitName(String ownerUnitName) {
        this.ownerUnitName = ownerUnitName;
    }

    public String getNotifUnitName() {
        return notifUnitName;
    }

    public void setNotifUnitName(String notifUnitName) {
        this.notifUnitName = notifUnitName;
    }
}
