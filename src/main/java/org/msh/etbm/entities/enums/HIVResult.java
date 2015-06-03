package org.msh.etbm.entities.enums;

public enum HIVResult {
	POSITIVE,
	NEGATIVE,
	ONGOING,
	NOTDONE;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
