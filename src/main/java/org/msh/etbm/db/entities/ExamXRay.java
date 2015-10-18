package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.XRayBaseline;
import org.msh.etbm.db.enums.XRayEvolution;
import org.msh.etbm.db.enums.XRayResult;

import javax.persistence.*;

/**
 * Stores information about an X-Ray exam of a case
 * @author Ricardo Lima
 *
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("gen")
@Table(name="examxray")
public class ExamXRay extends CaseEvent {

	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private XRayResult result;
	
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private XRayEvolution evolution;
	
	@PropertyLog(operations={Operation.NEW})
	private XRayBaseline baseline;
	
	private Boolean destruction;
	
	@ManyToOne
	@JoinColumn(name="PRESENTATION_ID")
	@PropertyLog(operations={Operation.NEW}, messageKey="TbField.XRAYPRESENTATION")
	private FieldValue presentation;

	/**
	 * @return the baseline
	 */
	public XRayBaseline getBaseline() {
		return baseline;
	}

	/**
	 * @param baseline the baseline to set
	 */
	public void setBaseline(XRayBaseline baseline) {
		this.baseline = baseline;
	}

	/**
	 * Return the X-Ray result
	 * @return the result
	 */
	public XRayResult getResult() {
		return result;
	}

	/**
	 * Set the X-Ray result
	 * @param result the result to set
	 */
	public void setResult(XRayResult result) {
		this.result = result;
	}

	/**
	 * @return the evolution
	 */
	public XRayEvolution getEvolution() {
		return evolution;
	}

	/**
	 * @param evolution the evolution to set
	 */
	public void setEvolution(XRayEvolution evolution) {
		this.evolution = evolution;
	}

	/**
	 * @param presentation the presentation to set
	 */
	public void setPresentation(FieldValue presentation) {
		this.presentation = presentation;
	}

	/**
	 * @return the presentation
	 */
	public FieldValue getPresentation() {
		return presentation;
	}

	/**
	 * @return the destruction
	 */
	public Boolean getDestruction() {
		return destruction;
	}

	/**
	 * @param destruction the destruction to set
	 */
	public void setDestruction(Boolean destruction) {
		this.destruction = destruction;
	}
}
