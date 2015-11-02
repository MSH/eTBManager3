package org.msh.etbm.services.admin.units;

import org.msh.etbm.services.admin.AddressRequest;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Abstract class containing common information about an unit (laboratory or TB unit)
 *
 * Created by rmemoria on 28/10/15.
 */
public class UnitRequest implements TypedUnit {

    /**
     * The type indicates the kind of object to create
     */
    @NotNull
    private UnitType type;

    @NotNull
    private String name;

    private String customId;

    private boolean active;

    private String shipContactName;
    private String shipContactPhone;

    /**
     * Address
     */
    @NotNull
    private AddressRequest address;

    /**
     * Ship address
     */
    private AddressRequest shipAddress;

    private UUID supplierId;

    private UUID authorizerId;

    private boolean receiveFromManufacturer;

    /** LABORATORY EXCLUSIVE **/
    private boolean performCulture;
    private boolean performMicroscopy;
    private boolean performDst;
    private boolean performXpert;

    /** TB UNIT EXCLUSIVE **/
    private boolean tbFacility;
    private boolean mdrFacility;
    private boolean ntmFacility;
    private boolean notificationUnit;
    private boolean patientDispensing;
    private Integer numDaysOrder;


    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public AddressRequest getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(AddressRequest shipAddress) {
        this.shipAddress = shipAddress;
    }

    public UUID getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(UUID supplierId) {
        this.supplierId = supplierId;
    }

    public UUID getAuthorizerId() {
        return authorizerId;
    }

    public void setAuthorizerId(UUID authorizerId) {
        this.authorizerId = authorizerId;
    }

    public boolean isReceiveFromManufacturer() {
        return receiveFromManufacturer;
    }

    public void setReceiveFromManufacturer(boolean receiveFromManufacturer) {
        this.receiveFromManufacturer = receiveFromManufacturer;
    }

    public boolean isPerformCulture() {
        return performCulture;
    }

    public void setPerformCulture(boolean performCulture) {
        this.performCulture = performCulture;
    }

    public boolean isPerformMicroscopy() {
        return performMicroscopy;
    }

    public void setPerformMicroscopy(boolean performMicroscopy) {
        this.performMicroscopy = performMicroscopy;
    }

    public boolean isPerformDst() {
        return performDst;
    }

    public void setPerformDst(boolean performDst) {
        this.performDst = performDst;
    }

    public boolean isPerformXpert() {
        return performXpert;
    }

    public void setPerformXpert(boolean performXpert) {
        this.performXpert = performXpert;
    }

    public boolean isTbFacility() {
        return tbFacility;
    }

    public void setTbFacility(boolean tbFacility) {
        this.tbFacility = tbFacility;
    }

    public boolean isMdrFacility() {
        return mdrFacility;
    }

    public void setMdrFacility(boolean mdrFacility) {
        this.mdrFacility = mdrFacility;
    }

    public boolean isNtmFacility() {
        return ntmFacility;
    }

    public void setNtmFacility(boolean ntmFacility) {
        this.ntmFacility = ntmFacility;
    }

    public boolean isNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(boolean notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public boolean isPatientDispensing() {
        return patientDispensing;
    }

    public void setPatientDispensing(boolean patientDispensing) {
        this.patientDispensing = patientDispensing;
    }

    public Integer getNumDaysOrder() {
        return numDaysOrder;
    }

    public void setNumDaysOrder(Integer numDaysOrder) {
        this.numDaysOrder = numDaysOrder;
    }
}
