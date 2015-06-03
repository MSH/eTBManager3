/**
 * 
 */
package org.msh.etbm.entities.enums;

/**
 * Options on how user will notify the medicine in-take treatment monitoring.</p>
 * SIMPLE - Just check the day patient received medicine.<br/>
 * DETAILED - Must inform how patient received medicine - DOTS or self administered
 * 
 * @author Ricardo Memoria
 *
 */
public enum TreatMonitoringInput implements MessageKey { 
	SIMPLE, DETAILED;

	/** {@inheritDoc}
	 */
	public String getMessageKey() {
		return getClass().getSimpleName() + "." + toString();
	}
}