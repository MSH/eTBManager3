package org.msh.etbm.db.entities;

import org.msh.etbm.db.WSObject;

import javax.persistence.*;

/**
 * Created by rmemoria on 29/6/15.
 */
@Entity
@Table(name = "product")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("gen")
public class Product extends WSObject {

    private String name;

    private String abbrevName;


    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbrevName() {
        return abbrevName;
    }

    public void setAbbrevName(String abbrevName) {
        this.abbrevName = abbrevName;
    }
}
