package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.WorkspaceEntity;
import org.msh.etbm.services.admin.products.ProductType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by rmemoria on 29/6/15.
 */
@Entity
@Table(name = "product")
@Inheritance(strategy =  InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("gen")
public class Product extends WorkspaceEntity {

    @Column(length = 250)
    @PropertyLog(messageKey = "form.name")
    @NotNull
    @Size(min = 3, max = 250)
    private String name;

    @PropertyLog(messageKey = "form.shortName")
    @NotNull
    @Size(min = 1, max = 30)
    private String shortName;

    @Column(length = 50)
    @PropertyLog(messageKey = "form.customId")
    private String customId;

    private boolean active = true;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                '}';
    }

    @Override
    public String getDisplayString() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ProductType getType() {
        return ProductType.PRODUCT;
    }
}
