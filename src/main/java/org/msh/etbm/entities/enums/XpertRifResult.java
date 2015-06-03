package org.msh.etbm.entities.enums;

public enum XpertRifResult {
	RIF_DETECTED,
	RIF_NOT_DETECTED,
	RIF_INDETERMINATE;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
