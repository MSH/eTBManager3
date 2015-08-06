package org.msh.etbm.db.enums;

public enum RegimenPhase {
	INTENSIVE,
	CONTINUOUS;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}	
}
