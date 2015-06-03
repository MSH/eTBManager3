package org.msh.etbm.entities.enums;

public enum XpertResult {
	
	INVALID,
	ERROR,
	NO_RESULT,
	ONGOING,
	TB_NOT_DETECTED,
	TB_DETECTED,
    INVALID_NORESULT_ERROR;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
