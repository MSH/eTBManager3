package org.msh.etbm.entities;

/**
 * Interface that an entity must implement to record log of the transaction that created it
 * and the last transaction related to it
 * 
 * @author Ricardo Memoria
 *
 */
public interface Transactional {

	/**
	 * Get the last transaction log of the entity
	 * @return
	 */
	public TransactionLog getLastTransaction();
	
	/**
	 * Change the last transaction log executed on the entity
	 * @param transactionLog
	 */
	public void setLastTransaction(TransactionLog transactionLog);
}
