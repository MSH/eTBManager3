package org.msh.etbm.db.entities;

import org.msh.etbm.commons.date.Period;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name="treatmenthealthunit")
public class TreatmentHealthUnit implements Transactional {

    @Id
    private UUID id;

	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="CASE_ID")
	@NotNull
	private TbCase tbcase;
	
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="UNIT_ID")
	@NotNull
	private Tbunit tbunit;

	@Embedded
	private Period period = new Period();
	
	/**
	 * Point to the transaction log that contains information about the last time this entity was changed (updated or created)
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="lastTransaction_ID")
	@PropertyLog(ignore=true)
	private TransactionLog lastTransaction;
	
	
	// Ricardo: TEMPORARY UNTIL A SOLUTION IS FOUND. Just to attend a request from the XML data model to
	// map an XML node to a property in the model
	@Transient
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


	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public TbCase getTbcase() {
		return tbcase;
	}

	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
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

	/**
	 * @return the lastTransaction
	 */
	@Override
	public TransactionLog getLastTransaction() {
		return lastTransaction;
	}

	/**
	 * @param lastTransaction the lastTransaction to set
	 */
	@Override
	public void setLastTransaction(TransactionLog lastTransaction) {
		this.lastTransaction = lastTransaction;
	}
}
