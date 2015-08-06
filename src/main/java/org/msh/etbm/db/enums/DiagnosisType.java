package org.msh.etbm.db.enums;

public enum DiagnosisType {

	SUSPECT,
	CONFIRMED;

	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
