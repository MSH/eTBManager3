package org.msh.etbm.services.admin.units.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.msh.etbm.services.admin.AddressData;

/**
 * Detailed data to be returned by the Unit service about an unit
 * Created by rmemoria on 1/11/15.
 */
public class UnitDetailedData extends UnitItemData {
    private String customId;
    private boolean active;
    private String shipContactName;
    private String shipContactPhone;
    private AddressData address;
    private AddressData shipAddress;
    private UnitData supplier;
    private UnitData authorizer;
    private boolean receiveFromManufacturer;

    /**
     * laboratory part
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performCulture;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performMicroscopy;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performDst;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean performXpert;

    /**
     * TB unit part
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean tbFacility;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean drtbFacility;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean ntmFacility;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean notificationUnit;
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

    public UnitData getSupplier() {
        return supplier;
    }

    public void setSupplier(UnitData supplier) {
        this.supplier = supplier;
    }

    public UnitData getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(UnitData authorizer) {
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

    public Boolean getTbFacility() {
        return tbFacility;
    }

    public void setTbFacility(Boolean tbFacility) {
        this.tbFacility = tbFacility;
    }

    public Boolean getNtmFacility() {
        return ntmFacility;
    }

    public void setNtmFacility(Boolean ntmFacility) {
        this.ntmFacility = ntmFacility;
    }

    public Boolean getNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(Boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public Integer getNumDaysOrder() {
        return numDaysOrder;
    }

    public void setNumDaysOrder(Integer numDaysOrder) {
        this.numDaysOrder = numDaysOrder;
    }

    public Boolean getDrtbFacility() {
        return drtbFacility;
    }

    public void setDrtbFacility(Boolean drtbFacility) {
        this.drtbFacility = drtbFacility;
    }

}
