package org.msh.etbm.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.entities.enums.MedicineLine;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Entity
@Table(name="substance")
public class Substance extends WSObject implements Serializable {
	private static final long serialVersionUID = -4338147429349562711L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@Embedded
	@NotNull
	@PropertyLog(messageKey="form.name")
	private LocalizedNameComp name = new LocalizedNameComp();
	
	@Embedded
	@AttributeOverrides({
		@AttributeOverride(name="name1", column=@Column(name="ABBREV_NAME1",length=10)),
		@AttributeOverride(name="name2", column=@Column(name="ABBREV_NAME2",length=10))
	})
	@PropertyLog(messageKey="form.abbrevName")
	private LocalizedNameComp abbrevName = new LocalizedNameComp();
	
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

	public int compare(Substance sub) {
		Integer order1 = getPrevTreatmentOrder();
		Integer order2 = sub.getPrevTreatmentOrder();
		
		if ((order1 == null) && (order2 == null))
			return 0;
		
		if (order1 == null)
			return -1;
		else if (order2 == null)
			return 1;
		
		if (order1 < order2)
			return -1;
		if (order1 > order2)
			return 1;
		return 0;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	public LocalizedNameComp getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(LocalizedNameComp name) {
		this.name = name;
	}

	/**
	 * @return the abbrevName
	 */
	public LocalizedNameComp getAbbrevName() {
		return abbrevName;
	}

	/**
	 * @param abbrevName the abbrevName to set
	 */
	public void setAbbrevName(LocalizedNameComp abbrevName) {
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
