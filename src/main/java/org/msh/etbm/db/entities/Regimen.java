package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.WSObject;
import org.msh.etbm.db.enums.CaseClassification;
import org.msh.etbm.db.enums.RegimenPhase;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Entity
@Table(name="regimen")
public class Regimen extends WSObject {

	@Column(length=100, name="regimen_name")
	private String name;
	
	private CaseClassification caseClassification;

	@OneToMany(cascade={CascadeType.ALL})
	@JoinColumn(name="REGIMEN_ID")
	private List<MedicineRegimen> medicines = new ArrayList<MedicineRegimen>();
	
	@Transient
	private List<MedicineRegimen> intensivePhaseMedicines = null;
	@Transient
	private List<MedicineRegimen> continuousPhaseMedicines = null;

	@Column(length=50)
	@PropertyLog(messageKey="global.legacyId")
	private String legacyId;


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
	 * Return the number of months of the intensive phase
	 * @return
	 */
	public int getMonthsIntensivePhase() {
		return getMonthsPhase(RegimenPhase.INTENSIVE);
	}

	
	/**
	 * Return the number of months of the continuous phase
	 * @return
	 */
	public int getMonthsContinuousPhase() {
		return getMonthsPhase(RegimenPhase.CONTINUOUS);
	}
	
	
	/**
	 * Include a medicine regimen to the regimen and also update the intensive and continuous list
	 * @param mr
	 */
	public void addMedicine(MedicineRegimen mr) {
		getMedicines().add(mr);
		if (intensivePhaseMedicines != null) {
			if (mr.getPhase() == RegimenPhase.INTENSIVE)
				 intensivePhaseMedicines.add(mr);
			else continuousPhaseMedicines.add(mr);
		}
	}
	
	/**
	 * Remove a medicine in the regimen and also update the intensive and continuous phase list
	 * @param mr
	 */
	public void remMedicine(MedicineRegimen mr) {
		getMedicines().remove(mr);
		if (intensivePhaseMedicines != null) {
			if (mr.getPhase() == RegimenPhase.INTENSIVE)
				 intensivePhaseMedicines.remove(mr);
			else continuousPhaseMedicines.remove(mr);
		}
	}
	
	
	/**
	 * get medicines used in the intensive phase
	 * @return
	 */
	public List<MedicineRegimen> getIntensivePhaseMedicines() {
		if (intensivePhaseMedicines == null)
			createPhaseLists();
		return intensivePhaseMedicines;
	}
	

	/**
	 * Check if medicines are the same as medicines in an specific phase
	 * @param phase phase to check
	 * @param meds medicines to compare if they are in the regimen (or are compatible)
	 * @return true if medicines are the same as in the regimen phase, otherwise false
	 */
	public boolean compareMedicinesInPhase(RegimenPhase phase, List<Medicine> meds) {
		List<MedicineRegimen> lst;
		if (RegimenPhase.INTENSIVE.equals(phase))
			 lst = getIntensivePhaseMedicines();
		else lst = getContinuousPhaseMedicines();
		
		if (lst.size() != meds.size())
			return false;
		
		for (MedicineRegimen mr: lst) {
			if (!meds.contains(mr.getMedicine()))
				return false;
		}
		
		return true;
	}


	/**
	 * get medicines used in the continuous phase
	 * @return
	 */
	public List<MedicineRegimen> getContinuousPhaseMedicines() {
		if (continuousPhaseMedicines == null)
			createPhaseLists();
		return continuousPhaseMedicines;		
	}
	
	private void createPhaseLists() {
		continuousPhaseMedicines = new ArrayList<MedicineRegimen>();
		intensivePhaseMedicines = new ArrayList<MedicineRegimen>();
		for (MedicineRegimen mr: getMedicines()) {
			if (mr.getPhase() == RegimenPhase.INTENSIVE)
				 intensivePhaseMedicines.add(mr);
			else continuousPhaseMedicines.add(mr);
		}
	}
	

	/**
	 * Create a list with different months of treatment for the medicines in a regimen phase
	 * @param phase
	 * @return
	 */
	public List<Integer> groupMonthsTreatment(RegimenPhase phase) {
		List<Integer> months = new ArrayList<Integer>();
		for (MedicineRegimen medreg: getMedicines()) {
			if (medreg.getPhase() == phase) {
				Integer num = medreg.getMonthsTreatment();
				if ((num != null) && (!months.contains(num)))
					months.add(num);
			}
			
			Collections.sort(months, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					if (o1 < o2)
						return -1;
					else if (o1 > o2)
						return 1;
					return 0;
				}
			});
		}
		return months;
	}


	/**
	 * Create a list of {@link MedicineRegimen} from an specific phase with a number of months of treatment
	 * @param phase
	 * @param months
	 * @return
	 */
	public List<MedicineRegimen> groupMedicinesByMonthTreatment(RegimenPhase phase, int months) {
		List<MedicineRegimen> lst = new ArrayList<MedicineRegimen>();
		for (MedicineRegimen medreg: getMedicines()) {
			if (medreg.getPhase() == phase) {
				if ( medreg.getMonthsTreatment() >= months ) {
					lst.add(medreg);
				}
			}
		}
		return lst;
	}
	
	
	/**
	 * Return number of months of the phase
	 * @param phase
	 * @return
	 */
	public int getMonthsPhase(RegimenPhase phase) {
		int num=0;
		for (MedicineRegimen medreg: getMedicines()) {
			if ((medreg.getMonthsTreatment() > num) && (medreg.getPhase().equals(phase))) {
				if (medreg.getMonthsTreatment() > num)
					num = medreg.getMonthsTreatment();
			}
		}
		return num;
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
}
