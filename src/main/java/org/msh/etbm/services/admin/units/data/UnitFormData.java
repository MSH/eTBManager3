package org.msh.etbm.services.admin.units.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.msh.etbm.services.admin.AddressRequest;
import org.msh.etbm.services.admin.units.TypedUnit;
import org.msh.etbm.services.admin.units.UnitType;

import java.util.Optional;
import java.util.UUID;

/**
 * Abstract class containing common information about an unit (laboratory or TB unit)
 *
 * Created by rmemoria on 28/10/15.
 */
public class UnitFormData implements TypedUnit {

    /**
     * The type indicates the kind of object to create
     */
    private UnitType type;

    private Optional<String> name;

    private Optional<String> customId;

    private Optional<Boolean> active;

    private Optional<String> shipContactName;
    private Optional<String> shipContactPhone;

    /**
     * Address
     */
    private AddressRequest address;

    /**
     * Ship address
     */
    private AddressRequest shipAddress;

    private Optional<UUID> supplierId;

    private Optional<UUID> authorizerId;

    private Optional<Boolean> receiveFromManufacturer;

    /** LABORATORY EXCLUSIVE **/
    private Optional<Boolean> performCulture;
    private Optional<Boolean> performMicroscopy;
    private Optional<Boolean> performDst;
    private Optional<Boolean> performXpert;

    /** TB UNIT EXCLUSIVE **/
    private Optional<Boolean> tbFacility;
    private Optional<Boolean> drtbFacility;
    private Optional<Boolean> ntmFacility;
    private Optional<Boolean> notificationUnit;
    private Optional<Integer> numDaysOrder;

    @JsonIgnore
    @Override
    public UnitType getUnitType() {
        return type;
    }

    public void setUnitType(UnitType type) {
        this.type = type;
    }

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
    }

    public Optional<String> getName() {
        return name;
    }

    public void setName(Optional<String> name) {
        this.name = name;
    }

    public Optional<String> getCustomId() {
        return customId;
    }

    public void setCustomId(Optional<String> customId) {
        this.customId = customId;
    }

    public Optional<Boolean> getActive() {
        return active;
    }

    public void setActive(Optional<Boolean> active) {
        this.active = active;
    }

    public Optional<String> getShipContactName() {
        return shipContactName;
    }

    public void setShipContactName(Optional<String> shipContactName) {
        this.shipContactName = shipContactName;
    }

    public Optional<String> getShipContactPhone() {
        return shipContactPhone;
    }

    public void setShipContactPhone(Optional<String> shipContactPhone) {
        this.shipContactPhone = shipContactPhone;
    }

    public AddressRequest getAddress() {
        if (address == null) {
            address = new AddressRequest();
        }
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public AddressRequest getShipAddress() {
        if (shipAddress == null) {
            shipAddress = new AddressRequest();
        }
        return shipAddress;
    }

    public void setShipAddress(AddressRequest shipAddress) {
        this.shipAddress = shipAddress;
    }

    public Optional<UUID> getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Optional<UUID> supplierId) {
        this.supplierId = supplierId;
    }

    public Optional<UUID> getAuthorizerId() {
        return authorizerId;
    }

    public void setAuthorizerId(Optional<UUID> authorizerId) {
        this.authorizerId = authorizerId;
    }

    public Optional<Boolean> getReceiveFromManufacturer() {
        return receiveFromManufacturer;
    }

    public void setReceiveFromManufacturer(Optional<Boolean> receiveFromManufacturer) {
        this.receiveFromManufacturer = receiveFromManufacturer;
    }

    public Optional<Boolean> getPerformCulture() {
        return performCulture;
    }

    public void setPerformCulture(Optional<Boolean> performCulture) {
        this.performCulture = performCulture;
    }

    public Optional<Boolean> getPerformMicroscopy() {
        return performMicroscopy;
    }

    public void setPerformMicroscopy(Optional<Boolean> performMicroscopy) {
        this.performMicroscopy = performMicroscopy;
    }

    public Optional<Boolean> getPerformDst() {
        return performDst;
    }

    public void setPerformDst(Optional<Boolean> performDst) {
        this.performDst = performDst;
    }

    public Optional<Boolean> getPerformXpert() {
        return performXpert;
    }

    public void setPerformXpert(Optional<Boolean> performXpert) {
        this.performXpert = performXpert;
    }

    public Optional<Boolean> getTbFacility() {
        return tbFacility;
    }

    public void setTbFacility(Optional<Boolean> tbFacility) {
        this.tbFacility = tbFacility;
    }

    public Optional<Boolean> getNtmFacility() {
        return ntmFacility;
    }

    public void setNtmFacility(Optional<Boolean> ntmFacility) {
        this.ntmFacility = ntmFacility;
    }

    public Optional<Boolean> getNotificationUnit() {
        return notificationUnit;
    }

    public void setNotificationUnit(Optional<Boolean> notificationUnit) {
        this.notificationUnit = notificationUnit;
    }

    public Optional<Boolean> getDrtbFacility() {
        return drtbFacility;
    }

    public void setDrtbFacility(Optional<Boolean> drtbFacility) {
        this.drtbFacility = drtbFacility;
    }

    public Optional<Integer> getNumDaysOrder() {
        return numDaysOrder;
    }

    public void setNumDaysOrder(Optional<Integer> numDaysOrder) {
        this.numDaysOrder = numDaysOrder;
    }
}
