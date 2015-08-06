/**
 * 
 */
package org.msh.etbm.db.enums;

import org.msh.etbm.db.MessageKey;

/**
 * Settings on how case validation will be handled in the system
 * @author Ricardo Memoria
 *
 */
public enum CaseValidationOption implements MessageKey {

	DISABLED,
	ENABLED,
	REQUIRED_BEFORE_TREATMENT_START;

	/** {@inheritDoc}
	 */
	@Override
	public String getMessageKey() {
		return getClass().getSimpleName() + "." + toString();
	}
}
