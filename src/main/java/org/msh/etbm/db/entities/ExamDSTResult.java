package org.msh.etbm.db.entities;

import org.msh.etbm.db.enums.DstResult;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name="examdstresult")
public class ExamDSTResult  {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

	@ManyToOne
	@JoinColumn(name="SUBSTANCE_ID")
	private Substance substance;
	
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private ExamDST exam;
	
	private DstResult result;
	

	public Substance getSubstance() {
		return substance;
	}

	public void setSubstance(Substance substante) {
		this.substance = substante;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public DstResult getResult() {
		return result;
	}

	public void setResult(DstResult result) {
		this.result = result;
	}

	/**
	 * @return the exam
	 */
	public ExamDST getExam() {
		return exam;
	}

	/**
	 * @param exam the exam to set
	 */
	public void setExam(ExamDST exam) {
		this.exam = exam;
	}
}
