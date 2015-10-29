package org.msh.etbm.services.admin.units;

import org.msh.etbm.db.Address;
import org.msh.etbm.services.admin.AddressRequest;

import java.util.Date;
import java.util.UUID;

/**
 * Abstract class containing common information about an unit (laboratory or TB unit)
 *
 * Created by rmemoria on 28/10/15.
 */
public abstract class UnitRequest {
    private String name;
    private String customId;
    private boolean active;

    private String shipContactName;
    private String shipContactPhone;

    /**
     * Address
     */
    private AddressRequest address;

    /**
     * Ship address
     */
    private AddressRequest shipAddress;

    private UUID supplierId;

    private UUID authorizerId;

    private boolean receiveFromManufacturer;

    private Date inventoryStartDate;

    private Date inventoryCloseDate;

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

    public Date getInventoryStartDate() {
        return inventoryStartDate;
    }

    public void setInventoryStartDate(Date inventoryStartDate) {
        this.inventoryStartDate = inventoryStartDate;
    }

    public Date getInventoryCloseDate() {
        return inventoryCloseDate;
    }

    public void setInventoryCloseDate(Date inventoryCloseDate) {
        this.inventoryCloseDate = inventoryCloseDate;
    }
}
