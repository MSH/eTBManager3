package org.msh.etbm.entities;

import org.msh.etbm.commons.transactionlog.mapping.PropertyLog;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * Super entity class where all entities that support workspace must inherit from.
 * This class contains the workspace information and the transactions that created
 * the entity and update it for the last time
 * 
 * @author Ricardo Memoria
 *
 */
@MappedSuperclass
public class WSObject implements Serializable, Transactional {
	private static final long serialVersionUID = 179043557345585531L;

	/**
	 * The workspace of this entity
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="WORKSPACE_ID")
	@NotNull
	@PropertyLog(ignore=true)
	private Workspace workspace;
	
	/**
	 * Point to the transaction log that contains information about the last time this entity was changed (updated or created)
	 */
	@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="lastTransaction_ID")
	@PropertyLog(ignore=true)
	private TransactionLog lastTransaction;


	/**
	 * Get the workspace that the entity belongs to
	 * @return
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * Set the entity workspace
	 * @param workspace
	 */
	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	/* (non-Javadoc)
	 * @see org.msh.tb.entities.Transactional#getLastTransaction()
	 */
	@Override
	public TransactionLog getLastTransaction() {
		return lastTransaction;
	}

	/* (non-Javadoc)
	 * @see org.msh.tb.entities.Transactional#setLastTransaction(org.msh.tb.entities.TransactionLog)
	 */
	@Override
	public void setLastTransaction(TransactionLog transactionLog) {
		this.lastTransaction = transactionLog;
	}
}
