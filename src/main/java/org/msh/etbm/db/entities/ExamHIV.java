package org.msh.etbm.db.entities;

import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.enums.HIVResult;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Ricardo Mem�ria
 *
 * Records information about an HIV result during the treatment
 */
@Entity
@Inheritance(strategy= InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="DISCRIMINATOR", discriminatorType= DiscriminatorType.STRING)
@DiscriminatorValue("gen")
@Table(name="examhiv")
public class ExamHIV extends CaseData implements Serializable {
	private static final long serialVersionUID = 2237957846637585494L;

	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private HIVResult result;
	
	@Temporal(TemporalType.DATE)
	private Date startedARTdate;
	
	@Temporal(TemporalType.DATE)
	private Date startedCPTdate;
	
	@Column(length=100)
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private String laboratory;

	//usrivast
	//addition for kenya workspace
	private Integer cd4Count;
	
	@Temporal(TemporalType.DATE)
	private Date cd4StDate;

	@Temporal(TemporalType.DATE)
	private Date partnerResultDate;
	
	public boolean isPartnerPresent() {
		return partnerResultDate != null;
	}
	
	public void setPartnerPresent(boolean value) {
		if (!value)
			partnerResultDate = null;
	}
	
	
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
	
	public Integer getCd4Count() {
		return cd4Count;
	}

	public void setCd4Count(Integer cd4Count) {
		this.cd4Count = cd4Count;
	}

	public Date getCd4StDate() {
		return cd4StDate;
	}

	public void setCd4StDate(Date cd4StDate) {
		this.cd4StDate = cd4StDate;
	}

	public Date getPartnerResultDate() {
		return partnerResultDate;
	}

	public void setPartnerResultDate(Date partnerResultDate) {
		this.partnerResultDate = partnerResultDate;
	}		
}
