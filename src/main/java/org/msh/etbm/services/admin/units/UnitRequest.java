package org.msh.etbm.services.admin.units;

import org.msh.etbm.services.admin.AddressRequest;

import javax.swing.text.html.Option;
import javax.validation.constraints.NotNull;
import java.util.Optional;
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
    private Optional<UnitType> type;

    private Optional<String> name;

    private Optional<String> customId;

    private Optional<Boolean> active;

    private Optional<String> shipContactName;
    private Optional<String> shipContactPhone;

    /**
     * Address
     */
    private Optional<AddressRequest> address;

    /**
     * Ship address
     */
    private Optional<AddressRequest> shipAddress;

    private Optional<UUID> supplierId;

    private Optional<UUID> authorizerId;

    private Optional<Optional> receiveFromManufacturer;

    /** LABORATORY EXCLUSIVE **/
    private Optional<Boolean> performCulture;
    private Optional<Boolean> performMicroscopy;
    private Optional<Boolean> performDst;
    private Optional<Boolean> performXpert;

    /** TB UNIT EXCLUSIVE **/
    private Optional<Boolean> tbFacility;
    private Optional<Boolean> mdrFacility;
    private Optional<Boolean> ntmFacility;
    private Optional<Boolean> notificationUnit;
    private Optional<Boolean> patientDispensing;
    private Optional<Integer> numDaysOrder;

    @Override
    public UnitType getUnitType() {
        return type == null? null: type.get();
    }

    public void setUnitType(UnitType type) {
        this.type = Optional.of(type);
    }

    public Optional<UnitType> getType() {
        return type;
    }

    public void setType(Optional<UnitType> type) {
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

    public Optional<AddressRequest> getAddress() {
        return address;
    }

    public void setAddress(Optional<AddressRequest> address) {
        this.address = address;
    }

    public Optional<AddressRequest> getShipAddress() {
        return shipAddress;
    }

    public void setShipAddress(Optional<AddressRequest> shipAddress) {
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

    public Optional<Optional> getReceiveFromManufacturer() {
        return receiveFromManufacturer;
    }

    public void setReceiveFromManufacturer(Optional<Optional> receiveFromManufacturer) {
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

    public Optional<Boolean> getMdrFacility() {
        return mdrFacility;
    }

    public void setMdrFacility(Optional<Boolean> mdrFacility) {
        this.mdrFacility = mdrFacility;
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

    public Optional<Boolean> getPatientDispensing() {
        return patientDispensing;
    }

    public void setPatientDispensing(Optional<Boolean> patientDispensing) {
        this.patientDispensing = patientDispensing;
    }

    public Optional<Integer> getNumDaysOrder() {
        return numDaysOrder;
    }

    public void setNumDaysOrder(Optional<Integer> numDaysOrder) {
        this.numDaysOrder = numDaysOrder;
    }
}
