/**
 * 
 */
package org.msh.etbm.entities;

/**
 * Every entity class that store the client key must implement this method.
 * The client key is used temporarily by the system while synchronizing with the client
 * 
 * @author Ricardo Memoria
 *
 */
public interface SyncKey {

	/**
	 * Store the ID of the entity in the server side
	 * @return
	 */
	Integer getId();
	
	/**
	 * Store the ID of the entity from the client side (temporarily in memory)
	 * @return
	 */
	Integer getClientId();
}
