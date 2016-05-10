package org.msh.etbm.db.entities;

import org.msh.etbm.commons.date.Period;
import org.msh.etbm.db.CaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "treatmenthealthunit")
public class TreatmentHealthUnit extends CaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UNIT_ID")
	@NotNull
	private Tbunit tbunit;

	@Embedded
	private Period period = new Period();

	
	/**
	 * Indicate if case is being transferred to this health unit
	 */
	private boolean transferring;

	
	public boolean isTransferring() {
		return transferring;
	}


	public void setTransferring(boolean transferring) {
		this.transferring = transferring;
	}


	public Tbunit getTbunit() {
		return tbunit;
	}

	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}


	public Period getPeriod() {
		return period;
	}


	public void setPeriod(Period period) {
		this.period = period;
	}

}
