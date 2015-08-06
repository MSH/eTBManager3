package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;
import org.msh.etbm.db.enums.MedicineLine;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name="substance")
public class Substance extends WSObject implements Serializable {
	private static final long serialVersionUID = -4338147429349562711L;

	@NotNull
	@PropertyLog(messageKey="form.name")
	private String name;
	
	@PropertyLog(messageKey="form.abbrevName")
	private String abbrevName;
	
	private MedicineLine line;
	
	private boolean prevTreatmentForm;
	
	private boolean dstResultForm;
	
	@PropertyLog(messageKey="form.displayorder")
	private Integer prevTreatmentOrder;

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String legacyId;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) 
			return true;
		
		if (!(obj instanceof Substance))
			return false;
		
		return ((Substance)obj).getId().equals(getId());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAbbrevName().toString();
	}

	public void setLine(MedicineLine line) {
		this.line = line;
	}

	public MedicineLine getLine() {
		return line;
	}

	/**
	 * @return the prevTreatmentForm
	 */
	public boolean isPrevTreatmentForm() {
		return prevTreatmentForm;
	}

	/**
	 * @param prevTreatmentForm the prevTreatmentForm to set
	 */
	public void setPrevTreatmentForm(boolean prevTreatmentForm) {
		this.prevTreatmentForm = prevTreatmentForm;
	}

	/**
	 * @return the dstResultForm
	 */
	public boolean isDstResultForm() {
		return dstResultForm;
	}

	/**
	 * @param dstResultForm the dstResultForm to set
	 */
	public void setDstResultForm(boolean dstResultForm) {
		this.dstResultForm = dstResultForm;
	}

	/**
	 * @return the prevTreatmentOrder
	 */
	public Integer getPrevTreatmentOrder() {
		return prevTreatmentOrder;
	}

	/**
	 * @param prevTreatmentOrder the prevTreatmentOrder to set
	 */
	public void setPrevTreatmentOrder(Integer prevTreatmentOrder) {
		this.prevTreatmentOrder = prevTreatmentOrder;
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
	 * @return the abbrevName
	 */
	public String getAbbrevName() {
		return abbrevName;
	}

	/**
	 * @param abbrevName the abbrevName to set
	 */
	public void setAbbrevName(String abbrevName) {
		this.abbrevName = abbrevName;
	}

	/**
	 * @return the legacyId
	 */
	public String getLegacyId() {
		return legacyId;
	}

	/**
	 * @param legacyId the legacyId to set
	 */
	public void setLegacyId(String legacyId) {
		this.legacyId = legacyId;
	}
}
