package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WorkspaceData;
import org.msh.etbm.db.enums.CaseClassification;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="regimen")
public class Regimen extends WorkspaceData {

	@Column(length=100)
	private String name;
	
	private CaseClassification caseClassification;

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="REGIMEN_ID")
	private List<MedicineRegimen> medicines = new ArrayList<MedicineRegimen>();

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String customId;

	private Integer daysOfIntensivePhaseDuration;

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (!(obj instanceof Regimen))
			return false;
	
		return ((Regimen)obj).getId().equals(getId());
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (name != null? name: super.toString());
	}

	/**
	 * Include a medicine regimen to the regimen and also update the intensive and continuous list
	 * @param mr
	 */
	public void addMedicine(MedicineRegimen mr) {
		getMedicines().add(mr);
	}
	
	/**
	 * Remove a medicine in the regimen and also update the intensive and continuous phase list
	 * @param mr
	 */
	public void remMedicine(MedicineRegimen mr) {
		getMedicines().remove(mr);
	}

	/**
	 * Check if medicine is part of the regimen
	 * @param med
	 * @return
	 */
	public boolean isMedicineInRegimen(Medicine med) {
		for (MedicineRegimen aux: getMedicines()) {
			if (aux.getMedicine().equals(med)) {
				return true;
			}
		}
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MedicineRegimen> getMedicines() {
		return medicines;
	}

	public void setMedicines(List<MedicineRegimen> medicines) {
		this.medicines = medicines;
	}


	/**
	 * @return the mdrTreatment
	 */
	public boolean isMdrTreatment() {
		return CaseClassification.DRTB.equals(caseClassification);
	}


	/**
	 * @return the tbTreatment
	 */
	public boolean isTbTreatment() {
		return CaseClassification.TB.equals(caseClassification);
	}

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    /**
	 * @return the caseClassification
	 */
	public CaseClassification getCaseClassification() {
		return caseClassification;
	}


	/**
	 * @param caseClassification the caseClassification to set
	 */
	public void setCaseClassification(CaseClassification caseClassification) {
		this.caseClassification = caseClassification;
	}

	public Integer getIntensivePhaseDuration() {
		return daysOfIntensivePhaseDuration;
	}

	public void setIntensivePhaseDuration(Integer daysOfIntensivePhaseDuration) {
		this.daysOfIntensivePhaseDuration = daysOfIntensivePhaseDuration;
	}
}
