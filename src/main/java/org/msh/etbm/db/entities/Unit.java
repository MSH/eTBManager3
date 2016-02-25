package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.Address;
import org.msh.etbm.db.EntityState;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.services.admin.units.UnitType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Abstract class that defines generic properties of an unit in the system.
 * Actually, there are two types of units in the system - laboratories and TB units.
 *
 * Laboratories are responsible for performing test exams, while TB units participate directly
 * or indirectly in TB management (pharmacy, health unit, NTP, etc)
 *
 * Created by rmemoria on 29/6/15.
 */
@Entity
@Table(name = "unit")
@Inheritance(strategy =  InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("gen")
public abstract class Unit extends WorkspaceEntity implements EntityState {

    /**
     * The name of the unit
     */
    @PropertyLog(messageKey = "form.name", operations = {Operation.NEW})
    @NotNull
    @Size(min = 3, max = 250)
    private String name;

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    @PropertyLog(messageKey = "EntityState.ACTIVE")
    private boolean active;

    /**
     * Order contact name
     */
    @Column(length = 200)
    private String shipContactName;

    /**
     * Order contact phone
     */
    @Column(length = 200)
    private String shipContactPhone;

    /**
     * Unit address
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "ADDRESS")),
            @AttributeOverride(name = "complement", column = @Column(name = "addressCompl")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "zipcode"))
    })
    @AssociationOverrides({
            @AssociationOverride(name = "adminUnit", joinColumns = @JoinColumn(name = "ADMINUNIT_ID"))
    })
    @PropertyLog(messageKey = "Address", operations = {Operation.NEW})
    @NotNull
    private Address address;

    /**
     * Shipping address to receive commodities supplies
     */
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "address", column = @Column(name = "shipAddress")),
            @AttributeOverride(name = "complement", column = @Column(name = "shipAddressCompl")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "shipZipCode")),
    })
    @AssociationOverrides({
            @AssociationOverride(name = "adminUnit", joinColumns = @JoinColumn(name = "SHIP_ADMINUNIT_ID"))
    })
    @PropertyLog(messageKey = "Address.shipAddr", operations = {Operation.NEW})
    private Address shipAddress;


    /** INFORMATION ABOUT INVENTORY MANAGEMENT */


    /**
     * Indicate the unit that will supply medicines
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUPPLIER_ID")
    private Tbunit supplier;

    /**
     * The authorizer for orders
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHORIZERUNIT_ID")
    private Tbunit authorizer;

    /**
     * Indicate if the unit can register commodities received from manufacturer
     */
    private boolean receiveFromManufacturer;

    /**
     * Date when this TB unit started the medicine management
     */
    @Temporal(TemporalType.DATE)
    private Date inventoryStartDate;

    /**
     * Limit date to create movements for this unit. The movement has to equals or after this date.
     */
    @Temporal(TemporalType.DATE)
    private Date inventoryCloseDate;


    /**
     * Check if medicine management was already started for this TB Unit
     * @return
     */
    public boolean isMedicineManagementStarted() {
        return inventoryStartDate != null;
    }


    @Override
    public String getDisplayString() {
        return name;
    }

    public abstract UnitType getType();

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

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
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

    public Address getAddress() {
        if (address == null) {
            address = new Address();
        }
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getShipAddress() {
        if (shipAddress == null) {
            shipAddress = new Address();
        }
        return shipAddress;
    }

    public void setShipAddress(Address shipAddress) {
        this.shipAddress = shipAddress;
    }

    public Tbunit getSupplier() {
        return supplier;
    }

    public void setSupplier(Tbunit supplier) {
        this.supplier = supplier;
    }

    public Tbunit getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(Tbunit authorizer) {
        this.authorizer = authorizer;
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
