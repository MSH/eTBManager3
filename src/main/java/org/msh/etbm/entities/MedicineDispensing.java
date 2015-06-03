package org.msh.etbm.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name="medicinedispensing")
public class MedicineDispensing implements Serializable {
	private static final long serialVersionUID = 808211986483933780L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private Integer id;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="UNIT_ID")
	private Tbunit tbunit;
	
	@Temporal(TemporalType.DATE)
	@NotNull
	private Date dispensingDate;
	
	@ManyToMany
	@JoinTable(name="movements_dispensing",
			joinColumns={@JoinColumn(name="DISPENSING_ID")},
			inverseJoinColumns={@JoinColumn(name="MOVEMENT_ID")})
	private List<Movement> movements = new ArrayList<Movement>();
	
	@OneToMany(mappedBy="dispensing", cascade={CascadeType.ALL})
	private List<MedicineDispensingCase> patients = new ArrayList<MedicineDispensingCase>();

		

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * @return the tbunit
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}

	/**
	 * @param tbunit the tbunit to set
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}


	/**
	 * @return the dispensingDate
	 */
	public Date getDispensingDate() {
		return dispensingDate;
	}


	/**
	 * @param dispensingDate the dispensingDate to set
	 */
	public void setDispensingDate(Date dispensingDate) {
		this.dispensingDate = dispensingDate;
	}


	/**
	 * @return the movements
	 */
	public List<Movement> getMovements() {
		return movements;
	}


	/**
	 * @param movements the movements to set
	 */
	public void setMovements(List<Movement> movements) {
		this.movements = movements;
	}


	/**
	 * @return the patients
	 */
	public List<MedicineDispensingCase> getPatients() {
		return patients;
	}

	/**
	 * @param patients the patients to set
	 */
	public void setPatients(List<MedicineDispensingCase> patients) {
		this.patients = patients;
	}
}
