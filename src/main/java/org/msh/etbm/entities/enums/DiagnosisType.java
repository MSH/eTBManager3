package org.msh.etbm.entities.enums;

public enum DiagnosisType {

	SUSPECT,
	CONFIRMED;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
