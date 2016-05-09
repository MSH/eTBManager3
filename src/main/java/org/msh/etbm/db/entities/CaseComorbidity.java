package org.msh.etbm.db.entities;

import org.msh.etbm.db.CaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "casecomorbidity")
public class CaseComorbidity extends CaseEntity {

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "complement", column = @Column(name = "otherCaseComorbidity")),
			@AttributeOverride(name = "value", column = @Column(name = "comorbidity")) })
	private FieldValueComponent comorbidity;
	
	@Column(length = 100)
	private String duration;
	
	@Lob
	private String comment;
	

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}


	/**
	 * @return the comorbidity
	 */
	public FieldValueComponent getComorbidity() {
		return comorbidity;
	}

	/**
	 * @param comorbidity the comorbidity to set
	 */
	public void setComorbidity(FieldValueComponent comorbidity) {
		this.comorbidity = comorbidity;
	}

	/**
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

}
