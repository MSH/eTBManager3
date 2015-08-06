package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.MedicineCategory;
import org.msh.etbm.db.enums.MedicineLine;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("med")
public class Medicine extends Product {

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String legacyId;

	private MedicineCategory category;
	
	private MedicineLine line;

	@OneToMany(mappedBy="medicine", cascade={CascadeType.ALL})
	private List<MedicineComponent> components = new ArrayList<MedicineComponent>();

	public String getTbInfoKey() {
		return line != null? line.getKey(): null;
	}


	@Override
	public boolean equals(Object obj) {
		if (obj == this) 
			return true;
		
		if (!(obj instanceof Medicine))
			return false;
		
		return ((Medicine)obj).getId().equals(getId());
	}


	public MedicineCategory getCategory() {
		return category;
	}

	public void setCategory(MedicineCategory category) {
		this.category = category;
	}


	public MedicineLine getLine() {
		return line;
	}

	public void setLine(MedicineLine line) {
		this.line = line;
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

	public void setComponents(List<MedicineComponent> components) {
		this.components = components;
	}

	public List<MedicineComponent> getComponents() {
		return components;
	}

	/**
	 * Return medicine component, which contains stated substance
	 * @author A.M.
	 */
	public MedicineComponent getComponentBySubstance(Substance s) {
		MedicineComponent res = null;
		if (components!=null)
			for (MedicineComponent mc: components)
				if (mc.getSubstance().equals(s))
					res = mc;
		return res;
	}

}
