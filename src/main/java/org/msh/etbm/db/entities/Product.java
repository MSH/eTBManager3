package org.msh.etbm.db.entities;

import org.hibernate.EntityMode;
import org.hibernate.annotations.Tuplizer;
import org.hibernate.tuple.component.DynamicMapComponentTuplizer;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WorkspaceData;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmemoria on 29/6/15.
 */
@Entity
@Table(name = "product")
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("gen")
public class Product extends WorkspaceData {

    @Column(length = 250)
    private String name;

    private String shortName;

    @Column(length=50)
    @PropertyLog(messageKey="global.legacyId")
    private String customId;


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
}
