package org.msh.etbm.entities;

/**
 * Interface all entity must implement to control its state (active or inactive)
 * @author Ricardo Memoria
 *
 */
public interface EntityState {

	/**
	 * Return true if entity is active, otherwise false if the entity is inactive
	 * @return boolean value
	 */
	boolean isActive();

	/**
	 * Change entity state
	 * @param newState new entity state. true if active, false if not active
	 */
	void setActive(boolean newState);
}
