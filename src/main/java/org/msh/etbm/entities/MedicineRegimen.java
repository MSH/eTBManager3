package org.msh.etbm.entities;

import org.msh.etbm.entities.enums.RegimenPhase;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="medicineregimen")
public class MedicineRegimen implements Serializable {
	private static final long serialVersionUID = 442884632590945592L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	@JoinColumn(name="MEDICINE_ID")
	@NotNull
	private Medicine medicine;
	
	private Integer defaultDoseUnit;
	
	private Integer defaultFrequency;
	
	private RegimenPhase phase;
	
	private Integer monthsTreatment;
	
	@ManyToOne
	@JoinColumn(name="SOURCE_ID")
	@NotNull
	private Source defaultSource;


	public Source getDefaultSource() {
		return defaultSource;
	}

	public void setDefaultSource(Source defaultSource) {
		this.defaultSource = defaultSource;
	}

	public RegimenPhase getPhase() {
		return phase;
	}

	public void setPhase(RegimenPhase phase) {
		this.phase = phase;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Medicine getMedicine() {
		return medicine;
	}

	public void setMedicine(Medicine medicine) {
		this.medicine = medicine;
	}

	public Integer getDefaultDoseUnit() {
		return defaultDoseUnit;
	}

	public void setDefaultDoseUnit(Integer defaultDoseUnit) {
		this.defaultDoseUnit = defaultDoseUnit;
	}

	public Integer getDefaultFrequency() {
		return defaultFrequency;
	}

	public void setDefaultFrequency(Integer defaultFrequency) {
		this.defaultFrequency = defaultFrequency;
	}

	/**
	 * @return the monthsTreatment
	 */
	public Integer getMonthsTreatment() {
		return monthsTreatment;
	}

	/**
	 * @param monthsTreatment the monthsTreatment to set
	 */
	public void setMonthsTreatment(Integer monthsTreatment) {
		this.monthsTreatment = monthsTreatment;
	}

}
