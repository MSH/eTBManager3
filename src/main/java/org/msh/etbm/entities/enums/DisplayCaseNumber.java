package org.msh.etbm.entities.enums;

/**
 * Define options of how the system will display the case number of the patient
 * 
 * @author Ricardo Memoria
 *
 */
public enum DisplayCaseNumber implements MessageKey {

	CASE_ID,
	VALIDATION_NUMBER,
	USER_DEFINED;

	/** {@inheritDoc}
	 */
	@Override
	public String getMessageKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
