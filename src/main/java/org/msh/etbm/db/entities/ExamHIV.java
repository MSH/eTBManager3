package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.HIVResult;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Ricardo Memória
 *
 * Records information about an HIV result during the treatment
 */
@Entity
@Table(name="examhiv")
public class ExamHIV extends CaseEvent {

	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private HIVResult result;
	
	@Temporal(TemporalType.DATE)
	private Date startedARTdate;
	
	@Temporal(TemporalType.DATE)
	private Date startedCPTdate;
	
	@Column(length=100)
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private String laboratory;


	
	public boolean isARTstarted() {
		return startedARTdate != null;
	}
	
	public boolean isCPTstarted() {
		return startedCPTdate != null;
	}
	
	public void setCPTstarted(boolean value) {
		if (!value)
			startedCPTdate = null;
	}
	
	public void setARTstarted(boolean value) {
		if (!value)
			startedARTdate = null;
	}
	
	public String getLaboratory() {
		return laboratory;
	}

	public void setLaboratory(String laboratory) {
		this.laboratory = laboratory;
	}

	public HIVResult getResult() {
		return result;
	}

	public void setResult(HIVResult result) {
		this.result = result;
	}

	public Date getStartedARTdate() {
		return startedARTdate;
	}

	public void setStartedARTdate(Date startedARTdate) {
		this.startedARTdate = startedARTdate;
	}

	public Date getStartedCPTdate() {
		return startedCPTdate;
	}

	public void setStartedCPTdate(Date startedCPTdate) {
		this.startedCPTdate = startedCPTdate;
	}
}
