package org.msh.etbm.entities;

import org.msh.etbm.entities.enums.DstResult;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="examdstresult")
public class ExamDSTResult implements Serializable, SyncKey {
	private static final long serialVersionUID = -5594762900664251756L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

	@ManyToOne
	@JoinColumn(name="SUBSTANCE_ID")
	private Substance substance;
	
	@ManyToOne
	@JoinColumn(name="EXAM_ID")
	private ExamDST exam;
	
	private DstResult result;
	
	@Transient
	// Ricardo: TEMPORARY UNTIL A SOLUTION IS FOUND. Just to attend a request from the XML data model to
	// map an XML node to a property in the model
	private Integer clientId;
	
	/**
	 * @return
	 */
	public Integer getClientId() {
		return clientId;
	}
	
	/**
	 * @param clientId
	 */
	public void setClientId(Integer clientId) {
		this.clientId = clientId;
	}


	public Substance getSubstance() {
		return substance;
	}

	public void setSubstance(Substance substante) {
		this.substance = substante;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
