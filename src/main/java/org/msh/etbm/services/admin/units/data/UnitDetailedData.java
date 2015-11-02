package org.msh.etbm.services.admin.units.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.services.admin.AddressData;

/**
 * Created by rmemoria on 1/11/15.
 */
public class UnitDetailedData extends UnitItemData {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String customId;
    private boolean active;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shipContactName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String shipContactPhone;
    private AddressData address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AddressData shipAddress;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UnitItemData supplier;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UnitItemData authorizer;
    private boolean receiveFromManufacturer;

    /** laboratory part */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performCulture;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performMicroscopy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performDst;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performXpert;
    
    /** TB unit part */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean tbUnit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean mdrUnit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean ntmUnit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean notificationUnit;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean patientDispensing;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer numDaysOrder;

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getShipContactName() {
        return shipContactName;
    }

    public void setShipContactName(String shipContactName) {
        this.shipContactName = shipContactName;
    }

    public String getShipContactPhone() {
        return shipContactPhone;
    }

    public void setShipContactPhone(String shipContactPhone) {
        this.shipContactPhone = shipContactPhone;
    }

    public AddressData getAddress() {
        return address;
    }

    public void setAddress(AddressData address) {
        this.address = address;
    }

    public AddressData getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(AddressData shipAddress) {
        this.shipAddress = shipAddress;
    }

    public UnitItemData getSupplier() {
        return supplier;
    }

    public void setSupplier(UnitItemData supplier) {
        this.supplier = supplier;
    }

    public UnitItemData getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(UnitItemData authorizer) {
        this.authorizer = authorizer;
    }

    public boolean isReceiveFromManufacturer() {
        return receiveFromManufacturer;
    }

    public void setReceiveFromManufacturer(boolean receiveFromManufacturer) {
        this.receiveFromManufacturer = receiveFromManufacturer;
    }

    public Boolean getPerformCulture() {
        return performCulture;
    }

    public void setPerformCulture(Boolean performCulture) {
        this.performCulture = performCulture;
    }

    public Boolean getPerformMicroscopy() {
        return performMicroscopy;
    }

    public void setPerformMicroscopy(Boolean performMicroscopy) {
        this.performMicroscopy = performMicroscopy;
    }

    public Boolean getPerformDst() {
        return performDst;
    }

    public void setPerformDst(Boolean performDst) {
        this.performDst = performDst;
    }

    public Boolean getPerformXpert() {
        return performXpert;
    }

    public void setPerformXpert(Boolean performXpert) {
        this.performXpert = performXpert;
    }

    public Boolean getTbUnit() {
        return tbUnit;
    }

    public void setTbUnit(Boolean tbUnit) {
        this.tbUnit = tbUnit;
    }

    public Boolean getMdrUnit() {
        return mdrUnit;
    }

    public void setMdrUnit(Boolean mdrUnit) {
        this.mdrUnit = mdrUnit;
    }

    public Boolean getNtmUnit() {
        return ntmUnit;
    }

    public void setNtmUnit(Boolean ntmUnit) {
        this.ntmUnit = ntmUnit;
    }

    public Boolean getNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(Boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public Boolean getPatientDispensing() {
        return patientDispensing;
    }

    public void setPatientDispensing(Boolean patientDispensing) {
        this.patientDispensing = patientDispensing;
    }

    public Integer getNumDaysOrder() {
        return numDaysOrder;
    }

    public void setNumDaysOrder(Integer numDaysOrder) {
        this.numDaysOrder = numDaysOrder;
    }
}
