package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.Address;
import org.msh.etbm.db.EntityState;
import org.msh.etbm.db.WSObject;

import javax.persistence.*;

/**
 * Created by rmemoria on 29/6/15.
 */
@Entity
@Table(name = "unit")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("gen")
public abstract class Unit extends WSObject implements EntityState {

    @PropertyLog(messageKey="form.name", operations={Operation.NEW})
    private String name;

    @Column(length=100)
    @PropertyLog(messageKey="global.phoneNumber")
    private String phoneNumber;

    @Column(length=50)
    @PropertyLog(messageKey="global.legacyId")
    private String customId;

    @PropertyLog(messageKey="EntityState.ACTIVE")
    private boolean active;

    /**
     * Order contact name
     */
    @Column(length=200)
    private String shipContactName;

    /**
     * Order contact phone
     */
    @Column(length=200)
    private String shipContactPhone;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="address", column=@Column(name="ADDRESS")),
            @AttributeOverride(name="complement", column=@Column(name="addressCompl")),
            @AttributeOverride(name="zipCode", column=@Column(name="zipcode")),
    })
    @AssociationOverrides({
            @AssociationOverride(name="adminUnit", joinColumns=@JoinColumn(name="ADMINUNIT_ID"))
    })
    @PropertyLog(messageKey="Address", operations={Operation.NEW})
    private Address address;


    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="address", column=@Column(name="shipAddress")),
            @AttributeOverride(name="complement", column=@Column(name="shipAddressCompl")),
            @AttributeOverride(name="zipCode", column=@Column(name="shipZipCode")),
    })
    @AssociationOverrides({
            @AssociationOverride(name="adminUnit", joinColumns=@JoinColumn(name="SHIP_ADMINUNIT_ID"))
    })
    @PropertyLog(messageKey="Address.shipAddr", operations={Operation.NEW})
    private Address shipAddress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
}
