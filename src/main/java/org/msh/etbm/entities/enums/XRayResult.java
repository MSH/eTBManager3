package org.msh.etbm.entities.enums;

/**
 * Possible results for X-Ray exams
 * @author Ricardo Lima
 *
 */
public enum XRayResult {
	POSITIVE,
	NEGATIVE,
	NO_CHANGE,
	STABILIZED;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
