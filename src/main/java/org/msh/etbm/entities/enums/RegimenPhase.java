package org.msh.etbm.entities.enums;

public enum RegimenPhase {
	INTENSIVE,
	CONTINUOUS;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}	
}
