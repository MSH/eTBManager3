package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;
import org.msh.etbm.db.enums.TbField;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Stores data about a field value from TB forms
 * @author Ricardo Lima
 *
 */
@Entity
@Table(name="fieldvalue")
public class FieldValue extends WSObject implements Serializable {
	private static final long serialVersionUID = -754148519681677704L;

	@PropertyLog(messageKey="form.name", operations={Operation.NEW})
    @Column(length = 100)
	private String name;

	@PropertyLog(messageKey="form.abbrevName", operations={Operation.NEW})
    @Column(length = 30)
	private String shortName;

	@Column(length=20)
	@PropertyLog(messageKey="form.customId")
	private String customId;
	
	@PropertyLog(messageKey="TbField")
	private TbField field;
	
	private boolean other;
	
	@Column(length=100)
	private String otherDescription;

	@PropertyLog(messageKey="form.displayorder")
	private Integer displayOrder;
	

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof FieldValue))
			return false;
		
		return ((FieldValue)obj).getId().equals(getId());
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	/**
	 * @param customId the customId to set
	 */
	public void setCustomId(String customId) {
		this.customId = customId;
	}

	/**
	 * @return the customId
	 */
	public String getCustomId() {
		return customId;
	}

	/**
	 * @param field the field to set
	 */
	public void setField(TbField field) {
		this.field = field;
	}

	/**
	 * @return the field
	 */
	public TbField getField() {
		return field;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (getName() != null? name.toString(): super.toString());
	}

	/**
	 * @return the other
	 */
	public boolean isOther() {
		return other;
	}

	/**
	 * @param other the other to set
	 */
	public void setOther(boolean other) {
		this.other = other;
	}

	/**
	 * @return the otherDescription
	 */
	public String getOtherDescription() {
		return otherDescription;
	}

	/**
	 * @param otherDescription the otherDescription to set
	 */
	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}

	/**
	 * @return the displayOrder
	 */
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}
}
