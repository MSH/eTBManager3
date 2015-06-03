package org.msh.etbm.entities.enums;

public enum InfectionSite {
	PULMONARY,
	EXTRAPULMONARY,
	BOTH;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
