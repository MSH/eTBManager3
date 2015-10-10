package org.msh.etbm.db.entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="medicineregimen")
public class MedicineRegimen implements Serializable {
	private static final long serialVersionUID = 442884632590945592L;

	@Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = { @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
	private UUID id;

	@ManyToOne
	@JoinColumn(name="MEDICINE_ID")
	@NotNull
	private Medicine medicine;

	private Integer defaultDoseUnit;
	
	private Integer defaultFrequency;

	private Integer daysTreatment;
	
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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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
		return daysTreatment;
	}

	/**
	 * @param daysTreatment the monthsTreatment to set
	 */
	public void setMonthsTreatment(Integer daysTreatment) {
		this.daysTreatment = daysTreatment;
	}
}
