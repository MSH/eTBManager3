package org.msh.etbm.entities.enums;

public enum ValidationState {

	WAITING_VALIDATION,
	VALIDATED;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
