package org.msh.etbm.db.entities;

import org.hibernate.annotations.GenericGenerator;
import org.msh.etbm.commons.transactionlog.Operation;
import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;
import org.msh.etbm.db.Transactional;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

/**
 * Base entity class where other classes inherit from to store case data
 * where a date is required as reference
 * 
 * @author Ricardo Memoria
 *
 */
@MappedSuperclass
public class CaseData implements Transactional {

	@Id
    @GeneratedValue(generator = "uuid2", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(name = "uuid2", strategy = "uuid2", parameters = { @org.hibernate.annotations.Parameter(name = "uuid_gen_strategy_class", value = "org.hibernate.id.uuid.CustomVersionOneStrategy") })
    private UUID id;

	@Temporal(TemporalType.DATE)
	@Column(name="EVENT_DATE")
	@PropertyLog(operations={Operation.NEW, Operation.DELETE})
	private Date date;
	
	@Lob
	private String comments;
	
	@ManyToOne
	@JoinColumn(name="CASE_ID")
	@NotNull
	private TbCase tbcase;
	
	
	/**
	 * Point to the transaction log that contains information about the last time this entity was changed (updated or created)
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="lastTransaction_ID")
	@PropertyLog(ignore=true)
	private TransactionLog lastTransaction;
		
	
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

	/**
	 * Return month of treatment based on the start treatment date and the collected date
	 * @return
	 */
	public Integer getMonthTreatment() {
		Date dt = getDate();
		if (dt == null)
			return null;
		
		if (getTbcase() == null)
			return null;

		return tbcase.getMonthTreatment(dt);
	}

	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comment) {
		this.comments = comment;
	}

	public TbCase getTbcase() {
		return tbcase;
	}

	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
/*
	@Override
	public String toString() {
		if (date == null)
			return getTbcase().getPatient().getFullName();

		DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
		String s = dateFormat.format(getDate());
		return s + " - " + getTbcase().getPatient().getFullName();
	}
*/

	/**
	 * @return the lastTransaction
	 */
	public TransactionLog getLastTransaction() {
		return lastTransaction;
	}

	/**
	 * @param lastTransaction the lastTransaction to set
	 */
	public void setLastTransaction(TransactionLog lastTransaction) {
		this.lastTransaction = lastTransaction;
	}

}
