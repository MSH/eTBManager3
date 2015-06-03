package org.msh.etbm.entities.enums;

/**
 * X-Ray evolution options
 * @author Ricardo Memoria
 *
 */
public enum XRayEvolution {
	IMPROVED,
	PROGRESSED,
	STABLE,
	//====== ONLY FOR AZERBAIJAN ======
	NA;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
