package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WorkspaceData;
import org.msh.etbm.db.enums.MedicineLine;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


@Entity
@Table(name="substance")
public class Substance extends WorkspaceData {

	@NotNull
	@PropertyLog(messageKey="form.name")
	private String name;
	
	@PropertyLog(messageKey="form.abbrevName")
	private String shortName;
	
	private MedicineLine line;
	
	private boolean prevTreatmentForm;
	
	private boolean dstResultForm;
	
	@PropertyLog(messageKey="form.displayorder")
	private Integer prevTreatmentOrder;

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String customId;

	@Override
	public boolean equals(Object obj) {
		if (obj == this) 
			return true;
		
		if (!(obj instanceof Substance))
			return false;
		
		return ((Substance)obj).getId().equals(getId());
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
