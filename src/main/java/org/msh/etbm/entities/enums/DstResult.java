package org.msh.etbm.entities.enums;

public enum DstResult {
	NOTDONE,
	RESISTANT,
	SUSCEPTIBLE,
	CONTAMINATED,
	BASELINE,
	INTERMEDIATE,
	ERROR,
	NOTRESISTANT,
	ONGOING;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}
}
