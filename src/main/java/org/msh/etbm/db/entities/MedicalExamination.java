package org.msh.etbm.db.entities;

import org.msh.etbm.commons.entities.cmdlog.Operation;
import org.msh.etbm.commons.entities.cmdlog.PropertyLog;
import org.msh.etbm.db.enums.MedAppointmentType;
import org.msh.etbm.db.enums.YesNoType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 *
 * Records information about a medical examination of a case
 *
 * @author Ricardo Mem�ria
 *
 */
@Entity
@Table(name="medicalexamination")
public class MedicalExamination extends CaseEvent {

	@PropertyLog(operations={Operation.NEW})
	private Double weight;
	
	@PropertyLog(operations={Operation.NEW})
	private Double height;
	

	private MedAppointmentType appointmentType;

	private YesNoType usingPrescMedicines;
	
	@Column(length=200)
	private String reasonNotUsingPrescMedicines;
	
	@Column(length=100)
	private String responsible;
	
	@Column(length=100)
	private String positionResponsible;


	
	/**
	 * Calculate the BMI (using weight and height)
	 * @return BMI value
	 */
	public double getBMI() {
		if ((height == null) || (height == 0))
			return 0;
		double h = height / 100;
		return (weight == null? 0: weight/(h*h)); 
	}
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Double getHeight() {
		return height;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public YesNoType getUsingPrescMedicines() {
		return usingPrescMedicines;
	}

	public void setUsingPrescMedicines(YesNoType usingPrescMedicines) {
		this.usingPrescMedicines = usingPrescMedicines;
	}

	public String getReasonNotUsingPrescMedicines() {
		return reasonNotUsingPrescMedicines;
	}

	public void setReasonNotUsingPrescMedicines(String reasonNotUsingPrescMedicines) {
		this.reasonNotUsingPrescMedicines = reasonNotUsingPrescMedicines;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public MedAppointmentType getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(MedAppointmentType appointmentType) {
		this.appointmentType = appointmentType;
	}


	/**
	 * @return the positionResponsible
	 */
	public String getPositionResponsible() {
		return positionResponsible;
	}

	/**
	 * @param positionResponsible the positionResponsible to set
	 */
	public void setPositionResponsible(String positionResponsible) {
		this.positionResponsible = positionResponsible;
	}
	
}
