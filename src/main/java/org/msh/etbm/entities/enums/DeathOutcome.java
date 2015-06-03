package org.msh.etbm.entities.enums;

public enum DeathOutcome {
	TB,
	OTHER_CAUSES;
	
	public String getKey() {
		return getClass().getSimpleName().concat("." + name());
	}

}
