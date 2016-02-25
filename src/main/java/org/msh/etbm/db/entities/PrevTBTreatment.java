package org.msh.etbm.db.entities;

import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.enums.PrevTBTreatmentOutcome;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Entity
@Inheritance(strategy =  InheritanceType.JOINED)
@Table(name = "prevtbtreatment")
public class PrevTBTreatment extends CaseEntity {


	@Column(name = "TREATMENT_MONTH")
	private Integer month;
	
	@Column(name = "TREATMENT_YEAR")
	private int year;
	
	@Column(name = "OUTCOME_MONTH")
	private Integer outcomeMonth;
	
	@Column(name = "OUTCOME_YEAR")
	private Integer outcomeYear;
	
	@NotNull
	private PrevTBTreatmentOutcome outcome;

	@ManyToMany
	@JoinTable(name = "res_prevtbtreatment",
			joinColumns = {@JoinColumn(name = "PREVTBTREATMENT_ID")},
			inverseJoinColumns = {@JoinColumn(name = "SUBSTANCE_ID")})
	private List<Substance> substances = new ArrayList<Substance>();



	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public PrevTBTreatmentOutcome getOutcome() {
		return outcome;
	}

	public void setOutcome(PrevTBTreatmentOutcome outcome) {
		this.outcome = outcome;
	}

	public List<Substance> getSubstances() {
		return substances;
	}

	public void setSubstances(List<Substance> substances) {
		this.substances = substances;
	}

	/**
	 * @return the outcomeMonth
	 */
	public Integer getOutcomeMonth() {
		return outcomeMonth;
	}

	/**
	 * @param outcomeMonth the outcomeMonth to set
	 */
	public void setOutcomeMonth(Integer outcomeMonth) {
		this.outcomeMonth = outcomeMonth;
	}

	/**
	 * @return the outcomeYear
	 */
	public Integer getOutcomeYear() {
		return outcomeYear;
	}

	/**
	 * @param outcomeYear the outcomeYear to set
	 */
	public void setOutcomeYear(Integer outcomeYear) {
		this.outcomeYear = outcomeYear;
	}

	public boolean hasOutcomeDate() {
        return getOutcomeMonth() != null && getOutcomeYear() != null;
	}
	
}
