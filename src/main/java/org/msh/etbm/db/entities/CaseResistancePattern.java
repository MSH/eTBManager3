package org.msh.etbm.db.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Store the resistance patterns of a case from the DST and Xpert exam tests
 * 
 * @author Ricardo Memoria
 *
 */
@Entity
@Table(name="caseresistancepattern")
public class CaseResistancePattern {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

	/**
	 * The TB case 
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="case_id")
	private TbCase tbcase;

	/**
	 * The resistance pattern of the case
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="resistpattern_id")
	@NotNull
	private ResistancePattern resistancePattern;

	/**
	 * true if it's a pattern presented in the diagnostic, or false if it's
	 * a resistance pattern of the treatment
	 */
	private boolean diagnosis;

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}
	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}
	/**
	 * @return the resistancePattern
	 */
	public ResistancePattern getResistancePattern() {
		return resistancePattern;
	}
	/**
	 * @param resistancePattern the resistancePattern to set
	 */
	public void setResistancePattern(ResistancePattern resistancePattern) {
		this.resistancePattern = resistancePattern;
	}
	/**
	 * @return the diagnostic
	 */
	public boolean isDiagnosis() {
		return diagnosis;
	}
	/**
	 * @param diagnosis set the diagnosis
	 */
	public void setDiagnosis(boolean diagnosis) {
		this.diagnosis = diagnosis;
	}
	/**
	 * @return the id
	 */
	public UUID getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(UUID id) {
		this.id = id;
	}
}
