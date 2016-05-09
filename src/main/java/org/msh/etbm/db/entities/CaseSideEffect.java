package org.msh.etbm.db.entities;

import org.msh.etbm.db.CaseEntity;
import org.msh.etbm.db.enums.YesNoType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Holds information about a side effect of a TB case
 * @author Ricardo Lima
 *
 */
@Entity
@Table(name = "casesideeffect")
public class CaseSideEffect extends CaseEntity {

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "complement", column = @Column(name = "otherAdverseEffect")),
			@AttributeOverride(name = "value", column = @Column(name = "sideeffect")) })
	@NotNull
	private FieldValueComponent sideEffect;

	private String medicines;
	
	@Column(name = "SE_MONTH")
	private int month;
	
	private YesNoType resolved;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSTANCE_ID")
	private Substance substance;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBSTANCE2_ID")
	private Substance substance2;
	
	@Lob
	private String comment;
		

	/**
	 * @return the comments
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comments to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the resolved
	 */
	public YesNoType getResolved() {
		return resolved;
	}

	/**
	 * @param resolved the resolved to set
	 */
	public void setResolved(YesNoType resolved) {
		this.resolved = resolved;
	}

	/**
	 * @return the sideEffect
	 */
	public FieldValueComponent getSideEffect() {
		if (sideEffect == null) {
            sideEffect = new FieldValueComponent();
        }

		return sideEffect;
	}

	/**
	 * @param sideEffect the sideEffect to set
	 */
	public void setSideEffect(FieldValueComponent sideEffect) {
		this.sideEffect = sideEffect;
	}

	/**
	 * @return the medicines
	 */
	public String getMedicines() {
		return medicines;
	}

	/**
	 * @param medicines the medicines to set
	 */
	public void setMedicines(String medicines) {
		this.medicines = medicines;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return the substance
	 */
	public Substance getSubstance() {
		return substance;
	}

	/**
	 * @param substance the substance to set
	 */
	public void setSubstance(Substance substance) {
		this.substance = substance;
	}

	/**
	 * @return the substance2
	 */
	public Substance getSubstance2() {
		return substance2;
	}

	/**
	 * @param substance2 the substance2 to set
	 */
	public void setSubstance2(Substance substance2) {
		this.substance2 = substance2;
	}

}
